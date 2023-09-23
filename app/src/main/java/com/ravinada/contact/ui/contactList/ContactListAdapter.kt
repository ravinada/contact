package com.ravinada.contact.ui.contactList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ravinada.contact.R
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.databinding.ItemContactBinding
import com.ravinada.contact.ui.contactDetail.ContactDetailUiModel
import com.ravinada.contact.ui.contactList.ContactListFragment.Companion.CONTACT_DETAIL

class ContactListAdapter(private val itemDetailFragmentContainer: View?) :
    RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    private var list: List<Contact> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Contact>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: Contact) {
            with(binding) {
                textViewFullName.text = dataModel.name
                textViewEmail.text = dataModel.email
                val bundle = Bundle()
                bundle.putParcelable(
                    CONTACT_DETAIL,
                    ContactDetailUiModel(dataModel.name)
                )
                root.setOnClickListener {
                    if (itemDetailFragmentContainer != null) {
                        itemDetailFragmentContainer.findNavController()
                            .navigate(R.id.contactItemDetailFragment, bundle)
                    } else {
                        itemView.findNavController().navigate(
                            R.id.action_contactListFragment_to_contactDetailFragment,
                            bundle
                        )
                    }
                }
            }
        }
    }
}
