package org.ezpztv.androidtv.constant

sealed interface CustomMessage {
	data object RefreshCurrentItem : CustomMessage
	data object ActionComplete : CustomMessage
}
