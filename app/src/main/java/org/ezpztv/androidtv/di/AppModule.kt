package org.ezpztv.androidtv.di

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import org.ezpztv.androidtv.BuildConfig
import org.ezpztv.androidtv.auth.repository.ServerRepository
import org.ezpztv.androidtv.auth.repository.UserRepository
import org.ezpztv.androidtv.auth.repository.UserRepositoryImpl
import org.ezpztv.androidtv.data.eventhandling.SocketHandler
import org.ezpztv.androidtv.data.model.DataRefreshService
import org.ezpztv.androidtv.data.repository.CustomMessageRepository
import org.ezpztv.androidtv.data.repository.CustomMessageRepositoryImpl
import org.ezpztv.androidtv.data.repository.ItemMutationRepository
import org.ezpztv.androidtv.data.repository.ItemMutationRepositoryImpl
import org.ezpztv.androidtv.data.repository.NotificationsRepository
import org.ezpztv.androidtv.data.repository.NotificationsRepositoryImpl
import org.ezpztv.androidtv.data.repository.UserViewsRepository
import org.ezpztv.androidtv.data.repository.UserViewsRepositoryImpl
import org.ezpztv.androidtv.data.service.BackgroundService
import org.ezpztv.androidtv.integration.dream.DreamViewModel
import org.ezpztv.androidtv.ui.ScreensaverViewModel
import org.ezpztv.androidtv.ui.itemhandling.ItemLauncher
import org.ezpztv.androidtv.ui.navigation.Destinations
import org.ezpztv.androidtv.ui.navigation.NavigationRepository
import org.ezpztv.androidtv.ui.navigation.NavigationRepositoryImpl
import org.ezpztv.androidtv.ui.picture.PictureViewerViewModel
import org.ezpztv.androidtv.ui.playback.PlaybackControllerContainer
import org.ezpztv.androidtv.ui.playback.nextup.NextUpViewModel
import org.ezpztv.androidtv.ui.search.SearchFragmentDelegate
import org.ezpztv.androidtv.ui.search.SearchRepository
import org.ezpztv.androidtv.ui.search.SearchRepositoryImpl
import org.ezpztv.androidtv.ui.search.SearchViewModel
import org.ezpztv.androidtv.ui.startup.ServerAddViewModel
import org.ezpztv.androidtv.ui.startup.StartupViewModel
import org.ezpztv.androidtv.ui.startup.UserLoginViewModel
import org.ezpztv.androidtv.util.KeyProcessor
import org.ezpztv.androidtv.util.MarkdownRenderer
import org.ezpztv.androidtv.util.PlaybackHelper
import org.ezpztv.androidtv.util.apiclient.ReportingHelper
import org.ezpztv.androidtv.util.sdk.SdkPlaybackHelper
import org.ezpztv.androidtv.util.sdk.legacy
import org.jellyfin.apiclient.AppInfo
import org.jellyfin.apiclient.android
import org.jellyfin.apiclient.logging.AndroidLogger
import org.jellyfin.sdk.android.androidDevice
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo
import org.jellyfin.sdk.model.DeviceInfo
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.jellyfin.apiclient.Jellyfin as JellyfinApiClient
import org.jellyfin.sdk.Jellyfin as JellyfinSdk

val defaultDeviceInfo = named("defaultDeviceInfo")

val appModule = module {
	// New SDK
	single(defaultDeviceInfo) { androidDevice(get()) }
	single {
		createJellyfin {
			context = androidContext()

			// Add client info
			clientInfo = ClientInfo("Android TV", BuildConfig.VERSION_NAME)
			deviceInfo = get(defaultDeviceInfo)

			// Change server version
			minimumServerVersion = ServerRepository.minimumServerVersion
		}
	}

	single {
		// Create an empty API instance, the actual values are set by the SessionRepository
		get<JellyfinSdk>().createApi()
	}

	single { SocketHandler(get(), get(), get(), get(), get(), get(), get(), get(), get()) }

	// Old apiclient
	single {
		JellyfinApiClient {
			appInfo = AppInfo("Android TV", BuildConfig.VERSION_NAME)
			logger = AndroidLogger()
			android(androidApplication())
		}
	}

	single {
		get<JellyfinApiClient>().createApi(
			device = get<DeviceInfo>(defaultDeviceInfo).legacy()
		)
	}

	// Coil (images)
	single {
		ImageLoader.Builder(androidContext()).apply {
			components {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) add(ImageDecoderDecoder.Factory())
				else add(GifDecoder.Factory())
				add(SvgDecoder.Factory())
			}
		}.build()
	}

	// Non API related
	single { DataRefreshService() }
	single { PlaybackControllerContainer() }

	single<UserRepository> { UserRepositoryImpl() }
	single<UserViewsRepository> { UserViewsRepositoryImpl(get()) }
	single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }
	single<ItemMutationRepository> { ItemMutationRepositoryImpl(get(), get()) }
	single<CustomMessageRepository> { CustomMessageRepositoryImpl() }
	single<NavigationRepository> { NavigationRepositoryImpl(Destinations.home) }
	single<SearchRepository> { SearchRepositoryImpl(get()) }

	viewModel { StartupViewModel(get(), get(), get(), get()) }
	viewModel { UserLoginViewModel(get(), get(), get(), get(defaultDeviceInfo)) }
	viewModel { ServerAddViewModel(get()) }
	viewModel { NextUpViewModel(get(), get(), get(), get()) }
	viewModel { PictureViewerViewModel(get()) }
	viewModel { ScreensaverViewModel(get()) }
	viewModel { SearchViewModel(get()) }
	viewModel { DreamViewModel(get(), get(), get(), get(), get()) }

	single { BackgroundService(get(), get(), get(), get(), get()) }

	single { MarkdownRenderer(get()) }
	single { ItemLauncher() }
	single { KeyProcessor() }
	single { ReportingHelper() }
	single<PlaybackHelper> { SdkPlaybackHelper(get(), get(), get(), get(), get(), get(), get()) }

	factory { (context: Context) -> SearchFragmentDelegate(context, get(), get()) }
}
