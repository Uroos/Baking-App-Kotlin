package com.example.home.mybakingappone.onlinecheck

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object GeneralUrls {
    @JvmStatic
    var RECIPES_URL: String = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json"
    @JvmStatic
    var BUNDLE_RECYCLER_LAYOUT: String = "classname.recycler.layout"
}

fun isOnline(context: Context): Boolean {

    val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo? = cm.getActiveNetworkInfo()
    return netInfo != null && netInfo.isConnectedOrConnecting();
}
