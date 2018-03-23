package com.mijjnapps.swcharacters.tasks

import android.os.AsyncTask
import android.util.Log
import com.github.swapi4j.SwapiClient
import com.github.swapi4j.model.Person


class QueryCharacterTask(private val client: SwapiClient, private val query: String, private val listener: ResponseListener) : AsyncTask<Unit, Unit, List<Person>>() {

    private val TAG = "QueryCharacterTask"

    interface ResponseListener {
        fun onQuerySuccess(result: List<Person>)
        fun onQueryFailed()
    }

    override fun doInBackground(vararg params: Unit): List<Person>? = try {
        client.searchAllPersons(query)
    } catch (e: Exception) {
        Log.e(TAG, "Error querying characters for $query", e)
        null
    }

    override fun onPostExecute(result: List<Person>?) {
        super.onPostExecute(result)
        if (!isCancelled)
            if (result == null)
                listener.onQueryFailed()
            else
                listener.onQuerySuccess(result)
    }
}