package com.novaterm.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * NovaTerm Application class.
 * Entry point for Hilt dependency injection.
 */
@HiltAndroidApp
class NovaTermApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
