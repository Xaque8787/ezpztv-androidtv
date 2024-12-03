package org.ezpztv.androidtv.ui.browsing

import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.constant.GridDirection
import org.ezpztv.androidtv.constant.ImageType
import org.ezpztv.androidtv.constant.PosterSize
import org.ezpztv.androidtv.preference.LibraryPreferences
import org.ezpztv.androidtv.preference.PreferencesRepository
import org.ezpztv.androidtv.ui.preference.dsl.OptionsFragment
import org.ezpztv.androidtv.ui.preference.dsl.checkbox
import org.ezpztv.androidtv.ui.preference.dsl.enum
import org.ezpztv.androidtv.ui.preference.dsl.optionsScreen
import org.jellyfin.preference.store.PreferenceStore
import org.koin.android.ext.android.inject

class DisplayPreferencesScreen : OptionsFragment() {
	private val preferencesRepository: PreferencesRepository by inject()
	private val libraryPreferences: LibraryPreferences by lazy {
		preferencesRepository.getLibraryPreferences(preferencesId!!)
	}

	private val preferencesId by lazy { requireArguments().getString(ARG_PREFERENCES_ID) }
	private val allowViewSelection by lazy { requireArguments().getBoolean(ARG_ALLOW_VIEW_SELECTION) }

	override val stores: Array<PreferenceStore<*, *>>
		get() = arrayOf(libraryPreferences)

	override val screen by optionsScreen {
		setTitle(R.string.lbl_display_preferences)

		category {
			enum<PosterSize> {
				setTitle(R.string.lbl_image_size)
				bind(libraryPreferences, LibraryPreferences.posterSize)
			}
			enum<ImageType> {
				setTitle(R.string.lbl_image_type)
				bind(libraryPreferences, LibraryPreferences.imageType)
			}
			enum<GridDirection> {
				setTitle(R.string.grid_direction)
				bind(libraryPreferences, LibraryPreferences.gridDirection)
			}

			if (allowViewSelection) {
				checkbox {
					setTitle(R.string.enable_smart_view)
					contentOn = requireContext().getString(R.string.enable_smart_view_description)
					contentOff = contentOn

					bind(libraryPreferences, LibraryPreferences.enableSmartScreen)
				}
			}
		}
	}

	companion object {
		const val ARG_ALLOW_VIEW_SELECTION = "allow_view_selection"
		const val ARG_PREFERENCES_ID = "preferences_id"
	}
}