package org.ezpztv.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.ui.playback.PlaybackController
import org.ezpztv.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.ezpztv.androidtv.ui.playback.overlay.LeanbackOverlayFragment
import org.ezpztv.androidtv.ui.playback.overlay.VideoPlayerAdapter

class RecordAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	companion object {
		const val INDEX_INACTIVE = 0
		const val INDEX_RECORDING = 1
	}

	init {
		val recordInactive = ContextCompat.getDrawable(context, R.drawable.ic_record)
		val recordActive = ContextCompat.getDrawable(context, R.drawable.ic_record_red)

		setDrawables(arrayOf(recordInactive, recordActive))
	}

	@Override
	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.toggleRecording()
	}
}
