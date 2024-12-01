package org.ezpztv.androidtv.di

import org.ezpztv.androidtv.util.ImageHelper
import org.koin.dsl.module

val utilsModule = module {
	single { ImageHelper(get()) }
}
