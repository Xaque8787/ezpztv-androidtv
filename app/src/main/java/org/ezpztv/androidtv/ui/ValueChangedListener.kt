package org.ezpztv.androidtv.ui

fun interface ValueChangedListener<T> {
	fun onValueChanged(value: T)
}
