package org.ezpztv.androidtv.di

import org.ezpztv.androidtv.preference.LiveTvPreferences
import org.ezpztv.androidtv.preference.PreferencesRepository
import org.ezpztv.androidtv.preference.SystemPreferences
import org.ezpztv.androidtv.preference.TelemetryPreferences
import org.ezpztv.androidtv.preference.UserPreferences
import org.ezpztv.androidtv.preference.UserSettingPreferences
import org.koin.dsl.module

val preferenceModule = module {
	single { PreferencesRepository(get(), get(), get()) }

	single { LiveTvPreferences(get()) }
	single { UserSettingPreferences(get()) }
	single { UserPreferences(get()) }
	single { SystemPreferences(get()) }
	single { TelemetryPreferences(get()) }
}
