package com.mijjnapps.swcharacters.tasks

import android.os.AsyncTask
import android.util.Log
import com.mijjnapps.swcharacters.data.*

class LoadPersonFromDbTask(private val db: SwDatabase, private val name: String, private val listener: DbResponseListener<SwPerson>) : AsyncTask<Unit, Unit, SwPerson>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) = db.personsDao().getPerson(name)

    override fun onPostExecute(result: SwPerson) {
        super.onPostExecute(result)
        Log.i("LoadPersonFromDbTask",
                "Done loading $name from Db in ${System.currentTimeMillis() - startTime}ms")
        listener.onSuccess(result)
    }
}

class LoadPersonsFromDbTask(private val db: SwDatabase, private val listener: DbListResponseListener<SwPerson>) : AsyncTask<Unit, Unit, List<SwPerson>>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) = db.personsDao().getPersons()

    override fun onPostExecute(result: List<SwPerson>) {
        super.onPostExecute(result)
        Log.i("LoadPersonsFromDbTask",
                "Done loading all persons from Db in ${System.currentTimeMillis() - startTime}ms")
        listener.onSuccess(result)
    }
}

class SavePersonsInDbTask(private val db: SwDatabase, private val persons: List<SwPerson>) : AsyncTask<Unit, Unit, Unit>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) {
        val dao = db.personsDao()
        persons.forEach { dao.savePerson(it) }
    }

    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        Log.i("SavePersonsInDbTask",
                "Done storing all persons in Db in ${System.currentTimeMillis() - startTime}ms")
    }
}

class LoadPlanetFromDbTask(private val db: SwDatabase, private val id: Long, private val listener: DbResponseListener<SwPlanet?>) : AsyncTask<Unit, Unit, SwPlanet?>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) = db.planetDao().getPlanet(id)

    override fun onPostExecute(result: SwPlanet?) {
        super.onPostExecute(result)
        Log.i("LoadPlanetFromDbTask",
                "Done loading planet $id from Db in ${System.currentTimeMillis() - startTime}ms")
        listener.onSuccess(result)
    }
}


class SavePlanetInDbTask(private val db: SwDatabase, private val planet: SwPlanet) : AsyncTask<Unit, Unit, Unit>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) {
        db.planetDao().savePlanet(planet)
    }

    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        Log.i("SavePlanetInDbTask",
                "Done storing planet in Db in ${System.currentTimeMillis() - startTime}ms")
    }
}


class LoadVehicleFromDbTask(private val db: SwDatabase, private val id: Long, private val listener: DbResponseListener<SwVehicle?>) : AsyncTask<Unit, Unit, SwVehicle?>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) = db.vehicleDao().getVehicle(id)

    override fun onPostExecute(result: SwVehicle?) {
        super.onPostExecute(result)
        Log.i("LoadVehicleFromDbTask",
                "Done loading vehicle $id from Db in ${System.currentTimeMillis() - startTime}ms")
        listener.onSuccess(result)
    }
}


class SaveVehicleInDbTask(private val db: SwDatabase, private val vehicle: SwVehicle) : AsyncTask<Unit, Unit, Unit>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) {
        db.vehicleDao().saveVehicle(vehicle)
    }

    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        Log.i("SaveVehicleInDbTask",
                "Done storing vehicle in Db in ${System.currentTimeMillis() - startTime}ms")
    }
}


class LoadFilmFromDbTask(private val db: SwDatabase, private val id: Long, private val listener: DbResponseListener<SwFilm?>) : AsyncTask<Unit, Unit, SwFilm?>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) = db.filmDao().getFilm(id)

    override fun onPostExecute(result: SwFilm?) {
        super.onPostExecute(result)
        Log.i("LoadFilmFromDbTask",
                "Done loading film $id from Db in ${System.currentTimeMillis() - startTime}ms")
        listener.onSuccess(result)
    }
}


class SaveFilmInDbTask(private val db: SwDatabase, private val film: SwFilm) : AsyncTask<Unit, Unit, Unit>() {
    private val startTime = System.currentTimeMillis()

    override fun doInBackground(vararg params: Unit?) {
        db.filmDao().saveFilm(film)
    }

    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        Log.i("SaveFilmInDbTask",
                "Done storing film in Db in ${System.currentTimeMillis() - startTime}ms")
    }
}