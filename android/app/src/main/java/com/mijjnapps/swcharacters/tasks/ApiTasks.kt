package com.mijjnapps.swcharacters.tasks

import android.os.AsyncTask
import android.util.Log
import com.github.swapi4j.SwapiClient
import com.github.swapi4j.model.*
import com.mijjnapps.swcharacters.data.SwFilm
import com.mijjnapps.swcharacters.data.SwPerson
import com.mijjnapps.swcharacters.data.SwPlanet
import com.mijjnapps.swcharacters.data.SwVehicle

class RefreshAllPersonsTask(private val client: SwapiClient, private val listener: ApiListResponseListener<SwPerson>) : AsyncTask<Unit, Unit, List<Person>>() {

    companion object {
        private val TAG = "RefreshAllPersonsTask"
    }

    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?): List<Person>? = try {
        client.allPersons
    } catch (e: Exception) {
        Log.e(TAG, "Person loading error: ", e)
        null
    }

    override fun onPostExecute(result: List<Person>?) {
        super.onPostExecute(result)
        //Can take up to 12 seconds
        Log.i(TAG, "Done loading all persons from API in ${System.currentTimeMillis() - startTime}ms")
        if (result == null)
            listener.onFail()
        else
            listener.onSuccess(result.map { SwPerson(it) })
    }
}

class QueryCharacterTask(private val client: SwapiClient, private val query: String, private val listener: ApiListResponseListener<SwPerson>) : AsyncTask<Unit, Unit, List<Person>>() {

    private val TAG = "QueryCharacterTask"

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
                listener.onFail()
            else
                listener.onSuccess(result.map { SwPerson(it) })
    }
}

class PersonPageTask(private val client: SwapiClient, private val pageNumber: Int, private val listener: PageResponseListener) : AsyncTask<Unit, Unit, Page<Person>>() {

    private val TAG = "PersonPageTask"

    interface PageResponseListener {
        fun onPageSuccess(result: Page<Person>)
        fun onPageFailed()
    }

    override fun doInBackground(vararg params: Unit): Page<Person>? = try {
        client.getPersons(pageNumber)
    } catch (e: Exception) {
        Log.e(TAG, "Error loading personsDao page $pageNumber", e)
        null
    }

    override fun onPostExecute(result: Page<Person>?) {
        super.onPostExecute(result)
        if (!isCancelled)
            if (result == null)
                listener.onPageFailed()
            else
                listener.onPageSuccess(result)
    }
}

class LoadPlanetFromApiTask(private val client: SwapiClient, private val planetId: Long, private val listener: ApiResponseListener<SwPlanet>) : AsyncTask<Unit, Unit, Planet>() {

    companion object {
        private val TAG = "LoadPlanetFromApiTask"
    }

    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?): Planet? = try {
        client.getPlanetById(planetId)
    } catch (e: Exception) {
        Log.e(TAG, "Planet loading error: ", e)
        null
    }

    override fun onPostExecute(result: Planet?) {
        super.onPostExecute(result)
        Log.i(TAG, "Done loading planet $planetId from API in ${System.currentTimeMillis() - startTime}ms")
        if (result == null)
            listener.onFail()
        else
            listener.onSuccess(SwPlanet(result))
    }
}

class LoadVehicleFromApiTask(private val client: SwapiClient, private val vehicleId: Long, private val listener: ApiResponseListener<SwVehicle>) : AsyncTask<Unit, Unit, Vehicle>() {

    companion object {
        private val TAG = "LoadVehicleFromApiTask"
    }

    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?): Vehicle? = try {
        client.getVehicleById(vehicleId)
    } catch (e: Exception) {
        Log.e(TAG, "Vehicle loading error: ", e)
        null
    }

    override fun onPostExecute(result: Vehicle?) {
        super.onPostExecute(result)
        Log.i(TAG, "Done loading vehicle $vehicleId from API in ${System.currentTimeMillis() - startTime}ms")
        if (result == null)
            listener.onFail()
        else
            listener.onSuccess(SwVehicle(result))
    }
}

class LoadFilmFromApiTask(private val client: SwapiClient, private val filmId: Long, private val listener: ApiResponseListener<SwFilm>) : AsyncTask<Unit, Unit, Film>() {

    companion object {
        private val TAG = "LoadFilmFromApiTask"
    }

    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?): Film? = try {
        client.getFilmById(filmId)
    } catch (e: Exception) {
        Log.e(TAG, "Film loading error: ", e)
        null
    }

    override fun onPostExecute(result: Film?) {
        super.onPostExecute(result)
        Log.i(TAG, "Done loading film $filmId from API in ${System.currentTimeMillis() - startTime}ms")
        if (result == null)
            listener.onFail()
        else
            listener.onSuccess(SwFilm(result))
    }
}