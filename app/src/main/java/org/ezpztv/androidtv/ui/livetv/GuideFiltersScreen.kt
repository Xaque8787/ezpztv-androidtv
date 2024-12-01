package org.ezpztv.androidtv.ui.livetv

import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.preference.SystemPreferences
import org.ezpztv.androidtv.ui.preference.dsl.OptionsFragment
import org.ezpztv.androidtv.ui.preference.dsl.OptionsItemCheckbox
import org.ezpztv.androidtv.ui.preference.dsl.checkbox
import org.ezpztv.androidtv.ui.preference.dsl.optionsScreen
import org.koin.android.ext.android.inject

class GuideFiltersScreen : OptionsFragment() {
	private val systemPreferences: SystemPreferences by inject()

	override val screen by optionsScreen {
		setTitle(R.string.lbl_filters)

		category {
			setOf(
				SystemPreferences.liveTvGuideFilterMovies to getString(R.string.lbl_movies),
				SystemPreferences.liveTvGuideFilterSeries to getString(R.string.lbl_series),
				SystemPreferences.liveTvGuideFilterNews to getString(R.string.lbl_news),
				SystemPreferences.liveTvGuideFilterKids to getString(R.string.lbl_kids),
				SystemPreferences.liveTvGuideFilterSports to getString(R.string.lbl_sports),
				SystemPreferences.liveTvGuideFilterPremiere to getString(R.string.lbl_new_only),
			).forEach { (preference, label) ->
				checkbox {
					title = label
					type = OptionsItemCheckbox.Type.SWITCH
					bind(systemPreferences, preference)
				}
			}
		}
	}
}
