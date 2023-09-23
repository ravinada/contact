package com.ravinada.contact.ui.contactList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.ravinada.contact.R
import com.ravinada.contact.databinding.FragmentContactListBinding
import com.ravinada.contact.ui.base.BaseFragment
import com.ravinada.contact.ui.base.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        inflater.inflate(R.menu.menu_main, menu)
        inflater.inflate(R.menu.menu_sort, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.searchContact_hint)
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
        }
    }

    private fun setupObserver() {
        viewModel.uiState.collectLatestRepeatOnStarted {
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

    companion object {
        const val CONTACT_DETAIL = "contact_detail"
    }
}