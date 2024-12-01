package org.ezpztv.androidtv.ui.home

import android.content.Context
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.Row
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.ui.playback.MediaManager
import org.ezpztv.androidtv.ui.presentation.CardPresenter
import org.ezpztv.androidtv.ui.presentation.MutableObjectAdapter

class HomeFragmentNowPlayingRow(
	private val mediaManager: MediaManager
) : HomeFragmentRow {
	private var row: ListRow? = null

	override fun addToRowsAdapter(
		context: Context,
		cardPresenter: CardPresenter,
		rowsAdapter: MutableObjectAdapter<Row>
	) {
		update(context, rowsAdapter)
	}

	fun update(context: Context, rowsAdapter: MutableObjectAdapter<Row>) {
		if (mediaManager.hasAudioQueueItems()) {
			// Ensure row exists
			if (row == null) row = ListRow(
				HeaderItem(context.getString(R.string.lbl_now_playing)),
				mediaManager.managedAudioQueue
			)
			// Add row if it wasn't added already
			if (!rowsAdapter.contains(row!!)) rowsAdapter.add(0, row!!)
		} else if (row != null) {
			rowsAdapter.remove(row!!)
			row = null
		}
	}
}
