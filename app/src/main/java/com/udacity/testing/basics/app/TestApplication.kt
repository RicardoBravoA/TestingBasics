package com.udacity.testing.basics.app

import android.app.Application
import com.udacity.testing.basics.BuildConfig
import timber.log.Timber

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}