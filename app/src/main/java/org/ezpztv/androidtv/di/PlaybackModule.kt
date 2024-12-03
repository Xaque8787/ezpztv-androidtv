package org.ezpztv.androidtv.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import org.ezpztv.androidtv.BuildConfig
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.preference.UserPreferences
import org.ezpztv.androidtv.preference.UserSettingPreferences
import org.ezpztv.androidtv.ui.browsing.MainActivity
import org.ezpztv.androidtv.ui.playback.GarbagePlaybackLauncher
import org.ezpztv.androidtv.ui.playback.MediaManager
import org.ezpztv.androidtv.ui.playback.RewritePlaybackLauncher
import org.ezpztv.androidtv.ui.playback.VideoQueueManager
import org.ezpztv.androidtv.ui.playback.rewrite.RewriteMediaManager
import org.jellyfin.playback.core.playbackManager
import org.jellyfin.playback.jellyfin.jellyfinPlugin
import org.jellyfin.playback.media3.exoplayer.ExoPlayerOptions
import org.jellyfin.playback.media3.exoplayer.exoPlayerPlugin
import org.jellyfin.playback.media3.session.MediaSessionOptions
import org.jellyfin.playback.media3.session.media3SessionPlugin
import org.jellyfin.sdk.api.client.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.module
import kotlin.time.Duration.Companion.milliseconds
import org.ezpztv.androidtv.ui.playback.PlaybackManager as LegacyPlaybackManager

val playbackModule = module {
	single { LegacyPlaybackManager(get()) }
	single { VideoQueueManager() }
	single<MediaManager> { RewriteMediaManager(get(), get(), get(), get()) }

	factory {
		val preferences = get<UserPreferences>()
		val useRewrite = preferences[UserPreferences.playbackRewriteVideoEnabled] && BuildConfig.DEVELOPMENT

		if (useRewrite) RewritePlaybackLauncher()
		else GarbagePlaybackLauncher(get())
	}

	single { createPlaybackManager() }
}

fun Scope.createPlaybackManager() = playbackManager(androidContext()) {
	val activityIntent = Intent(get(), MainActivity::class.java)
	val pendingIntent = PendingIntent.getActivity(get(), 0, activityIntent, PendingIntent.FLAG_IMMUTABLE)

	val notificationChannelId = "session"
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		val channel = NotificationChannel(
			notificationChannelId,
			notificationChannelId,
			NotificationManager.IMPORTANCE_LOW
		)
		NotificationManagerCompat.from(get()).createNotificationChannel(channel)
	}

	val userPreferences = get<UserPreferences>()
	val api = get<ApiClient>()
	val exoPlayerOptions = ExoPlayerOptions(
		httpConnectTimeout = api.httpClientOptions.connectTimeout,
		httpReadTimeout = api.httpClientOptions.requestTimeout,
		preferFfmpeg = userPreferences[UserPreferences.preferExoPlayerFfmpeg],
		enableDebugLogging = userPreferences[UserPreferences.debuggingEnabled],
	)
	install(exoPlayerPlugin(get(), exoPlayerOptions))

	val mediaSessionOptions = MediaSessionOptions(
		channelId = notificationChannelId,
		notificationId = 1,
		iconSmall = R.drawable.app_icon_foreground,
		openIntent = pendingIntent,
	)
	install(media3SessionPlugin(get(), mediaSessionOptions))

	install(jellyfinPlugin(get()))

	// Options
	val userSettingPreferences = get<UserSettingPreferences>()
	defaultRewindAmount = { userSettingPreferences[UserSettingPreferences.skipBackLength].milliseconds }
	defaultFastForwardAmount = { userSettingPreferences[UserSettingPreferences.skipForwardLength].milliseconds }
}