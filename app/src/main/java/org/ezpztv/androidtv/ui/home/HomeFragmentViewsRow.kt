package org.ezpztv.androidtv.ui.home

import android.content.Context
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.Row
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.data.querying.GetUserViewsRequest
import org.ezpztv.androidtv.ui.itemhandling.ItemRowAdapter
import org.ezpztv.androidtv.ui.presentation.CardPresenter
import org.ezpztv.androidtv.ui.presentation.MutableObjectAdapter
import org.ezpztv.androidtv.ui.presentation.UserViewCardPresenter

class HomeFragmentViewsRow(
	val small: Boolean,
) : HomeFragmentRow {
	private companion object {
		val smallCardPresenter = UserViewCardPresenter(true)
		val largeCardPresenter = UserViewCardPresenter(false)
	}

	override fun addToRowsAdapter(context: Context, cardPresenter: CardPresenter, rowsAdapter: MutableObjectAdapter<Row>) {
		val presenter = if (small) smallCardPresenter else largeCardPresenter
		val rowAdapter = ItemRowAdapter(context, GetUserViewsRequest, presenter, rowsAdapter)

		val header = HeaderItem(context.getString(R.string.lbl_my_media))
		val row = ListRow(header, rowAdapter)
		rowAdapter.setRow(row)
		rowAdapter.Retrieve()
		rowsAdapter.add(row)
	}
}
