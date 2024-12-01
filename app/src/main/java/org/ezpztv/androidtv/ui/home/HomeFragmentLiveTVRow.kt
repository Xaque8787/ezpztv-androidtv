package org.ezpztv.androidtv.ui.home

import android.app.Activity
import android.content.Context
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.auth.repository.UserRepository
import org.ezpztv.androidtv.constant.LiveTvOption
import org.ezpztv.androidtv.ui.GridButton
import org.ezpztv.androidtv.ui.navigation.Destinations
import org.ezpztv.androidtv.ui.navigation.NavigationRepository
import org.ezpztv.androidtv.ui.presentation.CardPresenter
import org.ezpztv.androidtv.ui.presentation.GridButtonPresenter
import org.ezpztv.androidtv.ui.presentation.MutableObjectAdapter
import org.ezpztv.androidtv.util.Utils

class HomeFragmentLiveTVRow(
	private val activity: Activity,
	private val userRepository: UserRepository,
	private val navigationRepository: NavigationRepository,
) : HomeFragmentRow, OnItemViewClickedListener {
	override fun addToRowsAdapter(context: Context, cardPresenter: CardPresenter, rowsAdapter: MutableObjectAdapter<Row>) {
		val header = HeaderItem(rowsAdapter.size().toLong(), activity.getString(R.string.pref_live_tv_cat))
		val adapter = ArrayObjectAdapter(GridButtonPresenter())

		// Live TV Guide button
		adapter.add(GridButton(LiveTvOption.LIVE_TV_GUIDE_OPTION_ID, activity.getString(R.string.lbl_live_tv_guide)))
		// Live TV Recordings button
		adapter.add(GridButton(LiveTvOption.LIVE_TV_RECORDINGS_OPTION_ID, activity.getString(R.string.lbl_recorded_tv)))
		if (Utils.canManageRecordings(userRepository.currentUser.value)) {
			// Recording Schedule button
			adapter.add(GridButton(LiveTvOption.LIVE_TV_SCHEDULE_OPTION_ID, activity.getString(R.string.lbl_schedule)))
			// Recording Series button
			adapter.add(GridButton(LiveTvOption.LIVE_TV_SERIES_OPTION_ID, activity.getString(R.string.lbl_series)))
		}

		rowsAdapter.add(ListRow(header, adapter))
	}

	override fun onItemClicked(itemViewHolder: Presenter.ViewHolder?, item: Any?, rowViewHolder: RowPresenter.ViewHolder?, row: Row?) {
		if (item !is GridButton) return

		when (item.id) {
			LiveTvOption.LIVE_TV_GUIDE_OPTION_ID -> navigationRepository.navigate(Destinations.liveTvGuide)
			LiveTvOption.LIVE_TV_SCHEDULE_OPTION_ID -> navigationRepository.navigate(Destinations.liveTvSchedule)
			LiveTvOption.LIVE_TV_RECORDINGS_OPTION_ID -> navigationRepository.navigate(Destinations.liveTvRecordings)
			LiveTvOption.LIVE_TV_SERIES_OPTION_ID -> navigationRepository.navigate(Destinations.liveTvSeriesRecordings)
		}
	}
}
