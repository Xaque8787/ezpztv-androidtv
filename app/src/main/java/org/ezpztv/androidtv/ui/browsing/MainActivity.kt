package org.ezpztv.androidtv.ui.browsing

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.ezpztv.androidtv.auth.repository.SessionRepository
import org.ezpztv.androidtv.auth.repository.UserRepository
import org.ezpztv.androidtv.databinding.ActivityMainBinding
import org.ezpztv.androidtv.ui.ScreensaverViewModel
import org.ezpztv.androidtv.ui.background.AppBackground
import org.ezpztv.androidtv.ui.navigation.NavigationAction
import org.ezpztv.androidtv.ui.navigation.NavigationRepository
import org.ezpztv.androidtv.ui.screensaver.InAppScreensaver
import org.ezpztv.androidtv.ui.startup.StartupActivity
import org.ezpztv.androidtv.util.applyTheme
import org.ezpztv.androidtv.util.isMediaSessionKeyEvent
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : FragmentActivity() {
	private val navigationRepository by inject<NavigationRepository>()
	private val sessionRepository by inject<SessionRepository>()
	private val userRepository by inject<UserRepository>()
	private val screensaverViewModel by viewModel<ScreensaverViewModel>()

	private lateinit var binding: ActivityMainBinding

	private val backPressedCallback = object : OnBackPressedCallback(false) {
		override fun handleOnBackPressed() {
			if (navigationRepository.canGoBack) navigationRepository.goBack()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		applyTheme()

		super.onCreate(savedInstanceState)

		if (!validateAuthentication()) return

		screensaverViewModel.keepScreenOn.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
			.onEach { keepScreenOn ->
				if (keepScreenOn) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
				else window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
			}.launchIn(lifecycleScope)

		onBackPressedDispatcher.addCallback(this, backPressedCallback)
		if (savedInstanceState == null && navigationRepository.canGoBack) navigationRepository.reset(clearHistory = true)

		navigationRepository.currentAction
			.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
			.onEach { action ->
				handleNavigationAction(action)
				backPressedCallback.isEnabled = navigationRepository.canGoBack
				screensaverViewModel.notifyInteraction(false)
			}.launchIn(lifecycleScope)

		binding = ActivityMainBinding.inflate(layoutInflater)
		binding.background.setContent { AppBackground() }
		binding.screensaver.setContent { InAppScreensaver() }
		setContentView(binding.root)
	}

	override fun onResume() {
		super.onResume()

		if (!validateAuthentication()) return

		applyTheme()

		screensaverViewModel.activityPaused = false
	}

	private fun validateAuthentication(): Boolean {
		if (sessionRepository.currentSession.value == null || userRepository.currentUser.value == null) {
			Timber.w("Activity ${this::class.qualifiedName} started without a session, bouncing to StartupActivity")
			startActivity(Intent(this, StartupActivity::class.java))
			finish()
			return false
		}

		return true
	}

	override fun onPause() {
		super.onPause()

		screensaverViewModel.activityPaused = true
	}

	override fun onStop() {
		super.onStop()

		lifecycleScope.launch {
			Timber.d("MainActivity stopped")
			sessionRepository.restoreSession(destroyOnly = true)
		}
	}

	private fun handleNavigationAction(action: NavigationAction) {
		when (action) {
			// DestinationFragmentView actions
			is NavigationAction.NavigateFragment -> binding.contentView.navigate(action)
			NavigationAction.GoBack -> binding.contentView.goBack()

			// Others
			is NavigationAction.NavigateActivity -> {
				val destination = action.destination
				val intent = Intent(this@MainActivity, destination.activity.java)
				intent.putExtras(destination.extras)
				startActivity(intent)
				action.onOpened()
			}

			NavigationAction.Nothing -> Unit
		}
	}

	// Forward key events to fragments
	private fun Fragment.onKeyEvent(keyCode: Int, event: KeyEvent?): Boolean {
		var result = childFragmentManager.fragments.any { it.onKeyEvent(keyCode, event) }
		if (!result && this is View.OnKeyListener) result = onKey(currentFocus, keyCode, event)
		return result
	}

	private fun onKeyEvent(keyCode: Int, event: KeyEvent?): Boolean {
		// Ignore the key event that closes the screensaver
		if (screensaverViewModel.visible.value) {
			screensaverViewModel.notifyInteraction(canCancel = event?.action == KeyEvent.ACTION_UP)
			return true
		}

		return supportFragmentManager.fragments
			.any { it.onKeyEvent(keyCode, event) }
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean =
		onKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event)

	override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean =
		onKeyEvent(keyCode, event) || super.onKeyUp(keyCode, event)

	override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean =
		onKeyEvent(keyCode, event) || super.onKeyUp(keyCode, event)

	override fun onUserInteraction() {
		super.onUserInteraction()

		screensaverViewModel.notifyInteraction(false)
	}

	@Suppress("RestrictedApi") // False positive
	override fun dispatchKeyEvent(event: KeyEvent): Boolean {
		// Ignore the key event that closes the screensaver
		if (!event.isMediaSessionKeyEvent() && screensaverViewModel.visible.value) {
			screensaverViewModel.notifyInteraction(canCancel = event.action == KeyEvent.ACTION_UP)
			return true
		}

		@Suppress("RestrictedApi") // False positive
		return super.dispatchKeyEvent(event)
	}

	@Suppress("RestrictedApi") // False positive
	override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
		// Ignore the key event that closes the screensaver
		if (!event.isMediaSessionKeyEvent() && screensaverViewModel.visible.value) {
			screensaverViewModel.notifyInteraction(canCancel = event.action == KeyEvent.ACTION_UP)
			return true
		}

		@Suppress("RestrictedApi") // False positive
		return super.dispatchKeyShortcutEvent(event)
	}

	override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
		// Ignore the touch event that closes the screensaver
		if (screensaverViewModel.visible.value) {
			screensaverViewModel.notifyInteraction(true)
			return true
		}

		return super.dispatchTouchEvent(ev)
	}
}
