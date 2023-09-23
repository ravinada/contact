package com.ravinada.contact.ui.contactList

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ravinada.contact.R
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.databinding.FragmentContactListBinding
import com.ravinada.contact.ui.base.BaseFragment
import com.ravinada.contact.ui.base.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class ContactListFragment : BaseFragment<FragmentContactListBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding
        get() = FragmentContactListBinding::inflate

    private val viewModel: ContactListViewModel by viewModel()
    private lateinit var adapter: ContactListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_master, menu)
        inflater.inflate(R.menu.menu_sort, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.contactList_searchContact_hint)
        searchView.onActionViewExpanded()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission here
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchContactList(it) }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_aToz -> {
                viewModel.sortContactListInAtoZ()
                true
            }
            R.id.action_zToa -> {
                viewModel.sortContactListInZtoA()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData(view)
        setupObserver()
    }

    private fun setupData(view: View) {
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)
        with(binding) {
            rootContactList.addTapToDismissBehaviour()
            adapter = ContactListAdapter(itemDetailFragmentContainer)
            recyclerView.adapter = adapter
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun setupObserver() {
        viewModel.observeUiState.collectLatestRepeatOnStarted {
            when (it) {
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(it.data)
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    snackbar(binding.root, it.message.toString(), Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(contact: Contact, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.deleteDialog_title))
        builder.setMessage(getString(R.string.deleteDialog_subtitle))

        builder.setPositiveButton(getString(R.string.deleteDialog_positive_button)) { _, _ ->
            viewModel.deleteContact(contact)
        }

        builder.setNegativeButton(getString(R.string.deleteDialog_negative_button)) { _, _ ->
            adapter.notifyItemChanged(position)
        }

        val dialog = builder.create()
        dialog.show()
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // Not used for swipe-to-delete
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition

            val itemList: List<Contact>? = when (val uiState = viewModel.observeUiState.value) {
                is UiState.Success -> uiState.data
                else -> null
            }
            val removedItem = itemList?.get(position)
            if (removedItem != null) {
                showDeleteConfirmationDialog(removedItem, position)
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }

                val background = ColorDrawable(Color.RED)
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                val deleteIcon = ContextCompat.getDrawable(
                    recyclerView.context,
                    R.drawable.ic_delete
                )
                val iconWidth = deleteIcon?.intrinsicWidth ?: 0
                val iconHeight = deleteIcon?.intrinsicHeight ?: 0
                val iconMargin = (itemHeight - iconHeight) / 2
                val iconLeft = itemView.right - iconMargin - iconWidth
                val iconRight = itemView.right - iconMargin
                val iconTop = itemView.top + (itemHeight - iconHeight) / 2
                val iconBottom = iconTop + iconHeight

                deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon?.draw(c)
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
            c.drawRect(left, top, right, bottom, Paint().apply { color = Color.WHITE })
        }
    }

    companion object {
        const val CONTACT_DETAIL = "contact_detail"
    }
}