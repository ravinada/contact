package com.ravinada.contact.ui.contactDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ravinada.contact.R
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.databinding.FragmentContactDetailBinding
import com.ravinada.contact.ui.base.BaseFragment
import com.ravinada.contact.ui.contactList.ContactListFragment.Companion.CONTACT_DETAIL

class ContactDetailFragment : BaseFragment<FragmentContactDetailBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactDetailBinding
        get() = FragmentContactDetailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupListener()
    }

    private fun setupData() {
        with(binding) {
            arguments?.let { bundle ->
                bundle.getParcelable<Contact>(CONTACT_DETAIL)?.let { details ->
                    constraintLayoutContactDetail.isVisible = !bundle.isEmpty
                    editTextFullName.setText(details.name)
                    editTextUsername.setText(details.username)
                    editTextPhoneNumber.setText(details.phone)
                    editTextEmail.setText(details.email)
                    details.address.apply {
                        editTextAddress.setText(
                            getString(
                                R.string.contactDetail_addressFormat,
                                this.street,
                                this.city,
                                this.zipcode
                            )
                        )
                    }
                    editTextWebsite.setText(details.website)
                    details.company.apply {
                        editTextCompany.setText(
                            getString(
                                R.string.contactDetail_companyFormat,
                                this.name,
                                this.catchPhrase,
                                this.bs
                            )
                        )
                    }
                }
            }

        }
    }

    private fun setupListener() {
        with(binding) {
            constraintLayoutContactDetail.addTapToDismissBehaviour()
        }
    }
}