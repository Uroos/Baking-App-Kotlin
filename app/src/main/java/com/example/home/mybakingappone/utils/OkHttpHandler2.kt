package com.example.home.mybakingappone.utils

import android.os.AsyncTask
import android.support.annotation.Nullable
import com.example.home.mybakingappone.SimpleIdlingResource
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class OkHttpHandler2: AsyncTask<String, Void, String>() {

    lateinit var listener:OnUpdateListener
      var idlingResource: SimpleIdlingResource?=null

    interface OnUpdateListener {
        fun onUpdate(s:String?)
    }

    fun setUpdateListener( listener:OnUpdateListener, @Nullable idlingResource:SimpleIdlingResource?) {
        this.idlingResource=idlingResource
        this.listener = listener
    }
    override fun doInBackground(vararg s: String?): String? {
        if (idlingResource != null) {
            idlingResource!!.setIdleState(false);
        }
        var client: OkHttpClient = OkHttpClient()
        var response:Response
        var request = Request.Builder()
                .url(s[0])
                .build()
        try {
            response = client.newCall(request).execute()
            return response.body().string()
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s)
        if (listener != null) {
            if (idlingResource != null) {
                idlingResource!!.setIdleState(true)
            }
            listener.onUpdate(s)
        }
    }
}