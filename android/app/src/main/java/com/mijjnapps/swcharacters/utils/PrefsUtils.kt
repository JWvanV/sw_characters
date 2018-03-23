package com.mijjnapps.swcharacters.utils

import android.content.Context
import android.preference.PreferenceManager

object PrefsUtils {

//    private keys

    private fun p(c: Context?) = if (c == null) null else PreferenceManager.getDefaultSharedPreferences(c)



}