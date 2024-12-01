package org.ezpztv.androidtv.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import org.ezpztv.androidtv.R;
import org.ezpztv.androidtv.databinding.ProgramGridCellBinding;
import org.ezpztv.androidtv.ui.livetv.LiveTvGuide;
import org.ezpztv.androidtv.ui.livetv.LiveTvGuideFragment;
import org.ezpztv.androidtv.util.Utils;

public class GuidePagingButton extends RelativeLayout {

    public GuidePagingButton(Context context) {
        super(context);
    }

    public GuidePagingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuidePagingButton(Context context, final LiveTvGuide guide, int start, String label) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        ProgramGridCellBinding binding = ProgramGridCellBinding.inflate(inflater, this, true);
        binding.programName.setText(label);

        setBackgroundColor(Utils.getThemeColor(context, R.attr.buttonDefaultNormalBackground));
        setFocusable(true);
        setOnClickListener(v -> guide.displayChannels(start, LiveTvGuideFragment.PAGE_SIZE));
    }

    @Override
    protected void onFocusChanged(boolean hasFocus, int direction, Rect previouslyFocused) {
        super.onFocusChanged(hasFocus, direction, previouslyFocused);

        setBackgroundColor(Utils.getThemeColor(getContext(),
            hasFocus ? android.R.attr.colorAccent : R.attr.buttonDefaultNormalBackground));
    }
}
