package org.ezpztv.androidtv.di

import org.ezpztv.androidtv.auth.AccountManagerMigration
import org.ezpztv.androidtv.auth.apiclient.ApiBinder
import org.ezpztv.androidtv.auth.repository.AuthenticationRepository
import org.ezpztv.androidtv.auth.repository.AuthenticationRepositoryImpl
import org.ezpztv.androidtv.auth.repository.ServerRepository
import org.ezpztv.androidtv.auth.repository.ServerRepositoryImpl
import org.ezpztv.androidtv.auth.repository.ServerUserRepository
import org.ezpztv.androidtv.auth.repository.ServerUserRepositoryImpl
import org.ezpztv.androidtv.auth.repository.SessionRepository
import org.ezpztv.androidtv.auth.repository.SessionRepositoryImpl
import org.ezpztv.androidtv.auth.store.AuthenticationPreferences
import org.ezpztv.androidtv.auth.store.AuthenticationStore
import org.koin.dsl.module

val authModule = module {
	single { AccountManagerMigration(get()) }
	single { AuthenticationStore(get(), get()) }
	single { AuthenticationPreferences(get()) }

	single<AuthenticationRepository> {
		AuthenticationRepositoryImpl(get(), get(), get(), get(), get(), get(defaultDeviceInfo))
	}
	single<ServerRepository> { ServerRepositoryImpl(get(), get()) }
	single<ServerUserRepository> { ServerUserRepositoryImpl(get(), get()) }
	single<SessionRepository> {
		SessionRepositoryImpl(get(), get(), get(), get(), get(), get(defaultDeviceInfo), get(), get(), get())
	}

	single { ApiBinder(get(), get()) }
}
