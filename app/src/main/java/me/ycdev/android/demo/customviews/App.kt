package me.ycdev.android.demo.customviews

import android.app.Application
import android.content.Context
import timber.log.Timber

class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Timber.plant(Timber.DebugTree())
        Timber.tag(TAG).i("app start...")
    }

    companion object {
        private const val TAG = "IpcDemo"
    }
}