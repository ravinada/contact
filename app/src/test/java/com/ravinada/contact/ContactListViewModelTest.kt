package com.ravinada.contact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ravinada.contact.data.api.repository.ApiRepository
import com.ravinada.contact.data.api.response.Address
import com.ravinada.contact.data.api.response.Company
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.data.api.response.Geo
import com.ravinada.contact.ui.base.UiState
import com.ravinada.contact.ui.contactList.ContactListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ContactListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiRepository: ApiRepository
    private lateinit var viewModel: ContactListViewModel

    private val testData = listOf(
        Contact(
            id = 1, name = "Alice",
            address = Address("", Geo("", ""), "", "", ""),
            email = "", phone = "", username = "", website = "",
            company = Company("", "", "")
        ),
        Contact(
            id = 2, name = "Charlie",
            address = Address("", Geo("", ""), "", "", ""),
            email = "", phone = "", username = "", website = "",
            company = Company("", "", "")
        ),
        Contact(
            id = 3, name = "Bob",
            address = Address("", Geo("", ""), "", "", ""),
            email = "", phone = "", username = "", website = "",
            company = Company("", "", "")
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ContactListViewModel(apiRepository)
    }

    @Test
    fun testSortContactListInAtoZ() = runTest {
        viewModel.uiState.value = UiState.Success(testData)

        viewModel.sortContactListInAtoZ()

        val sortedData = (viewModel.observeUiState.value as UiState.Success<List<Contact>>).data
        assertEquals("Alice", sortedData[0].name)
        assertEquals("Bob", sortedData[1].name)
        assertEquals("Charlie", sortedData[2].name)
    }

    @Test
    fun testSortContactListInZtoA() = runTest {
        viewModel.uiState.value = UiState.Success(testData)

        viewModel.sortContactListInZtoA()

        val sortedData = (viewModel.observeUiState.value as UiState.Success<List<Contact>>).data
        assertEquals("Charlie", sortedData[0].name)
        assertEquals("Bob", sortedData[1].name)
        assertEquals("Alice", sortedData[2].name)
    }

    @Test
    fun testSearchContactList() = runTest {
        viewModel.uiState.value = UiState.Success(testData)

        viewModel.searchContactList("Char")

        val filteredData = (viewModel.observeUiState.value as UiState.Success<List<Contact>>).data
        assertEquals(1, filteredData.size)
        assertEquals("Charlie", filteredData[0].name)
    }

    @Test
    fun testFetchContactList() = runTest {
        val testDataFlow = flowOf(testData)
        `when`(apiRepository.getContactList()).thenReturn(testDataFlow)

        viewModel.fetchContactList()

        val fetchedData = (viewModel.observeUiState.value as UiState.Success<List<Contact>>).data
        assertEquals(testData, fetchedData)
    }
}
