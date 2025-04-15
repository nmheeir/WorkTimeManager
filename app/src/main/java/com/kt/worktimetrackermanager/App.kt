package com.kt.worktimetrackermanager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessaging
import com.kt.worktimetrackermanager.core.presentation.utils.DeviceTokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.set
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            //Must have
            Timber.plant(Timber.DebugTree())
        }
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
    }
}

class AppLifecycleTracker : Application.ActivityLifecycleCallbacks {
    private val activityStack = mutableListOf<String>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityStack.add(activity::class.java.simpleName)
        Timber.tag("ActivityStack").d("Created: ${activity::class.java.simpleName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityStack.remove(activity::class.java.simpleName)
        Timber.tag("ActivityStack").d("Destroyed: ${activity::class.java.simpleName}")
    }

    fun printActivityStack() {
        Timber.tag("ActivityStack").d("Current stack: $activityStack")
    }

    // Các phương thức còn lại có thể để trống
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}