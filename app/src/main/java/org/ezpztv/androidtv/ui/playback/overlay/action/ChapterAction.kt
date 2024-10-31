package org.ezpztv.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.View
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.ui.playback.PlaybackController
import org.ezpztv.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.ezpztv.androidtv.ui.playback.overlay.LeanbackOverlayFragment
import org.ezpztv.androidtv.ui.playback.overlay.VideoPlayerAdapter

class ChapterAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	init {
		initializeWithIcon(R.drawable.ic_select_chapter)
	}

	@Override
	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.leanbackOverlayFragment.hideOverlay()
		videoPlayerAdapter.masterOverlayFragment.showChapterSelector();
	}
}
