package com.mijjnapps.swcharacters.utils

import android.content.Context
import android.preference.PreferenceManager

object PrefsUtils {

//    private keys

    private const val KEY_PERSON_TOTAL = "personTotal"
    private const val KEY_PERSON_PAGES_LOADED = "personLoadedPages"
    private const val KEY_SORT_STYLE = "sortStyle"
    private const val KEY_FAVORITES = "favorites"

    private fun String.getValue(c: Context?, defaultValue: Boolean): Boolean =
            p(c)?.getBoolean(this, defaultValue) ?: defaultValue

    private fun String.getValue(c: Context?, defaultValue: Int): Int =
            p(c)?.getInt(this, defaultValue) ?: defaultValue

    private fun String.getValue(c: Context?, defaultValue: Long): Long =
            p(c)?.getLong(this, defaultValue) ?: defaultValue

    private fun String.getValue(c: Context?, defaultValue: String): String =
            p(c)?.getString(this, defaultValue) ?: defaultValue

    private fun String.getValue(c: Context?, defaultValue: Set<String>): Set<String> =
            p(c)?.getStringSet(this, defaultValue) ?: defaultValue

    private fun p(c: Context?) = if (c == null) null else PreferenceManager.getDefaultSharedPreferences(c)

//    /**
//     * PERSONTOTAL
//     */
//    fun getTotalPersons(c: Context?): Int = KEY_PERSON_TOTAL.getValue(c, -1)
//
//    fun setTotalPersons(c: Context?, count: Int) {
//        p(c)?.edit()?.putInt(KEY_PERSON_TOTAL, count)?.apply()
//    }
//
//    /**
//     * PERSONPAGESLOADED
//     */
//    fun getPersonPagesLoaded(c: Context?): Int = KEY_PERSON_PAGES_LOADED.getValue(c, 0)
//
//    fun setPersonPagesLoaded(c: Context?, lastPage: Int) {
//        p(c)?.edit()?.putInt(KEY_PERSON_PAGES_LOADED, lastPage)?.apply()
//    }

    /**
     * SORT
     */
    enum class SortType { NAME, BIRTHYEAR }

    fun getSortType(c: Context?): SortType = SortType.valueOf(KEY_SORT_STYLE.getValue(c, SortType.NAME.name))

    fun setSortType(c: Context?, type: SortType) {
        p(c)?.edit()?.putString(KEY_SORT_STYLE, type.name)?.apply()
    }

    /**
     * FAVORITES
     */
    fun getFavorites(c: Context?) = KEY_FAVORITES.getValue(c, HashSet())

    fun isFavorite(c: Context?, personName: String): Boolean = getFavorites(c).contains(personName)

    fun toggleFavorite(c: Context?, personName: String): Boolean {
        var favorites = getFavorites(c)
        val res: Boolean
        if (favorites.contains(personName)) {
            favorites = favorites.minus(personName)
            res = false
        } else {
            favorites = favorites.plus(personName)
            res = true
        }
        p(c)?.edit()?.putStringSet(KEY_FAVORITES, favorites)?.apply()
        return res
    }
}