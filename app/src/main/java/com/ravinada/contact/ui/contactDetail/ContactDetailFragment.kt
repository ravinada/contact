package com.ravinada.contact.ui.contactDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.ravinada.contact.R
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.databinding.FragmentContactDetailBinding
import com.ravinada.contact.ui.base.BaseFragment
import com.ravinada.contact.ui.contactList.ContactListFragment.Companion.CONTACT_DETAIL
import com.ravinada.contact.ui.contactList.ContactListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class ContactDetailFragment : BaseFragment<FragmentContactDetailBinding>() {

    private val viewModel: ContactListViewModel by viewModel()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactDetailBinding
        get() = FragmentContactDetailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                arguments?.getParcelable<Contact>(CONTACT_DETAIL)?.let {
                    showDeleteConfirmationDialog(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupData() {
        with(binding) {
            arguments?.getParcelable<Contact>(CONTACT_DETAIL)?.let { details ->
                constraintLayoutContactDetail.isVisible = true
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

    private fun showDeleteConfirmationDialog(contact: Contact) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.deleteDialog_title))
        builder.setMessage(getString(R.string.deleteDialog_subtitle))

        builder.setPositiveButton(getString(R.string.deleteDialog_positive_button)) { _, _ ->
            viewModel.deleteContact(contact)
            findNavController().navigateUp()
        }

        builder.setNegativeButton(getString(R.string.deleteDialog_negative_button)) { _, _ ->
            // nothing to do
        }

        val dialog = builder.create()
        dialog.show()
    }
}
