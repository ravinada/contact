package com.ravinada.contact.di

import android.app.Application
import android.content.Context
import com.ravinada.contact.R
import com.ravinada.contact.data.api.repository.Api
import com.ravinada.contact.data.api.repository.ApiRepository
import com.ravinada.contact.data.api.repository.ApiRepositoryImpl
import com.ravinada.contact.data.retrofit.NetworkBuilder
import com.ravinada.contact.ui.contactList.ContactListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {
    private val applicationModules = module {
        single {
            get<Application>().run {
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
            }
        }
    }

    private val viewModelModules = module {
        viewModel { ContactListViewModel(get()) }
    }

    private val networkModules = module {
        single { NetworkBuilder.create(NetworkBuilder.BASE_URL, Api::class.java) }
    }

    private val repositoryModules = module {
        single<ApiRepository> { ApiRepositoryImpl(get()) }
    }

    fun getAll() = listOf(applicationModules, viewModelModules, networkModules, repositoryModules)
}