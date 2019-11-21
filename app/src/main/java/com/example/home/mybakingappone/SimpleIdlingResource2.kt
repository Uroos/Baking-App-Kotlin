package com.example.home.mybakingappone

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class SimpleIdlingResource2 : IdlingResource {
    @Volatile
    var mCallback: IdlingResource.ResourceCallback? = null

    // Idleness is controlled with this boolean.
    private var mIsIdleNow: AtomicBoolean = AtomicBoolean(true)

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        mCallback = callback

    }

    override fun getName(): String {
        return this.name

    }

    override fun isIdleNow(): Boolean {
        return mIsIdleNow.get()
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    fun setIdleState(isIdleNow: Boolean) {
        mIsIdleNow.set(isIdleNow)
        if (isIdleNow && mCallback != null) {
            mCallback!!.onTransitionToIdle()
        }
    }
}