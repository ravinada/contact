package com.ravinada.contact.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ravinada.contact.BuildConfig
import com.ravinada.contact.R
import com.ravinada.contact.data.api.repository.Api
import com.ravinada.contact.data.api.repository.ApiRepository
import com.ravinada.contact.data.api.repository.ApiRepositoryImpl
import com.ravinada.contact.data.retrofit.NetworkBuilder
import com.ravinada.contact.data.room.AppDatabase
import com.ravinada.contact.data.room.RoomModule
import com.ravinada.contact.ui.MainViewModel
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
        single {
            Room.databaseBuilder(get(), AppDatabase::class.java, BuildConfig.DEFAULT_ROOM)
                .allowMainThreadQueries().fallbackToDestructiveMigration().build()
        }
    }

    private val viewModelModules = module {
        viewModel { ContactListViewModel(get()) }
        viewModel { MainViewModel(get()) }
    }

    private val networkModules = module {
        single { NetworkBuilder.create(NetworkBuilder.BASE_URL, Api::class.java) }
    }

    private val repositoryModules = module {
        single<ApiRepository> { ApiRepositoryImpl(get(), get()) }
    }

    private val daoModules = module {
        single { RoomModule(get()).getContactDbDao() }
    }

    fun getAll() =
        listOf(applicationModules, viewModelModules, networkModules, repositoryModules, daoModules)
}