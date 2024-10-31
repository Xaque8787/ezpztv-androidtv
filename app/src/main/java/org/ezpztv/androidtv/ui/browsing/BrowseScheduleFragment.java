package org.ezpztv.androidtv.ui.browsing;

import org.ezpztv.androidtv.ui.livetv.TvManager;
import org.ezpztv.androidtv.ui.presentation.CardPresenter;

public class BrowseScheduleFragment extends EnhancedBrowseFragment {

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupQueries(final RowLoader rowLoader) {
        TvManager.getScheduleRowsAsync(this, null, new CardPresenter(true), mRowsAdapter);
    }
}
