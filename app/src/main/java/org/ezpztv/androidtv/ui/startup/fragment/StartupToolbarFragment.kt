package org.ezpztv.androidtv.ui.startup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import org.ezpztv.androidtv.R
import org.ezpztv.androidtv.databinding.FragmentToolbarStartupBinding
import org.ezpztv.androidtv.ui.preference.PreferencesActivity
import org.ezpztv.androidtv.ui.preference.screen.AuthPreferencesScreen

class StartupToolbarFragment : Fragment() {
	private var _binding: FragmentToolbarStartupBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentToolbarStartupBinding.inflate(inflater, container, false)

		binding.help.setOnClickListener {
			parentFragmentManager.commit {
				addToBackStack(null)
				replace<ConnectHelpAlertFragment>(R.id.content_view)
			}
		}

		binding.settings.setOnClickListener {
			val intent = Intent(requireContext(), PreferencesActivity::class.java)
			intent.putExtra(PreferencesActivity.EXTRA_SCREEN, AuthPreferencesScreen::class.qualifiedName)
			intent.putExtra(PreferencesActivity.EXTRA_SCREEN_ARGS, bundleOf(
				AuthPreferencesScreen.ARG_SHOW_ABOUT to true
			))
			startActivity(intent)
		}

		return binding.root
	}

	override fun onDestroyView() {
		super.onDestroyView()

		_binding = null
	}
}
