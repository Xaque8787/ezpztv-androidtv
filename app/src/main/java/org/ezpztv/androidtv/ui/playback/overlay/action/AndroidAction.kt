package org.ezpztv.androidtv.ui.playback.overlay.action

import org.ezpztv.androidtv.ui.playback.overlay.VideoPlayerAdapter

interface AndroidAction {
	fun onActionClicked(
		videoPlayerAdapter: VideoPlayerAdapter
	)
}
