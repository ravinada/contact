package com.ravinada.contact.ui.contactDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ravinada.contact.databinding.FragmentContactDetailBinding
import com.ravinada.contact.ui.base.BaseFragment
import com.ravinada.contact.ui.contactList.ContactListFragment.Companion.CONTACT_DETAIL

class ContactDetailFragment : BaseFragment<FragmentContactDetailBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactDetailBinding
        get() = FragmentContactDetailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            bundle.getParcelable<ContactDetailUiModel>(CONTACT_DETAIL)?.let { details ->
                binding.itemDetail.text = details.name
            }
        }
    }
}