package org.ezpztv.androidtv.ui.preference.category

import android.os.Build
import org.ezpztv.androidtv.BuildConfig
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.ui.preference.dsl.OptionsScreen
import org.ezpztv.androidtv.ui.preference.dsl.link
import org.ezpztv.androidtv.ui.preference.screen.LicensesScreen

fun OptionsScreen.aboutCategory() = category {
	setTitle(R.string.pref_about_title)

	link {
		// Hardcoded strings for troubleshooting purposes
		title = "Jellyfin app version"
		content = "jellyfin-androidtv ${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}"
		icon = R.drawable.ic_jellyfin
	}

	link {
		setTitle(R.string.pref_device_model)
		content = "${Build.MANUFACTURER} ${Build.MODEL}"
		icon = R.drawable.ic_tv
	}

	link {
		setTitle(R.string.licenses_link)
		setContent(R.string.licenses_link_description)
		icon = R.drawable.ic_guide
		withFragment<LicensesScreen>()
	}
}
