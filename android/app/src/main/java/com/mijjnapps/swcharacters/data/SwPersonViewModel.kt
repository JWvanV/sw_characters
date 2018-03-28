package com.mijjnapps.swcharacters.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.github.swapi4j.SwapiClient
import com.mijjnapps.swcharacters.tasks.*

class SwPersonViewModel : ViewModel() {

    private val TAG = "SwPersonViewModel"

    private val client = SwapiClient()
    private lateinit var db: SwDatabase
    private var person: MutableLiveData<SwPerson>? = null
    private var planet: MutableLiveData<SwPlanet>? = null
    private var vehicles: MutableLiveData<List<SwVehicle>>? = null
    private var films: MutableLiveData<List<SwFilm>>? = null

    fun init(applicationContext: Context) {
        db = Room.databaseBuilder(
                applicationContext,
                SwDatabase::class.java,
                "swCharactersDb.db")
                .build()
    }

    /**
     * GETTERS
     */
    fun getPerson(name: String): MutableLiveData<SwPerson> {
        Log.d(TAG, "Get Person $name")
        if (person == null) {
            person = MutableLiveData()
            loadPerson(name)
        }
        return person!!
    }

    fun getPlanet(id: Long): MutableLiveData<SwPlanet> {
        if (planet == null) {
            planet = MutableLiveData()
            loadPlanet(id)
        }
        return planet!!
    }

    fun getVehicles(vehicleIds: String): MutableLiveData<List<SwVehicle>> {
        Log.d(TAG, "Get vehicles $vehicleIds")
        if (vehicles == null) {
            vehicles = MutableLiveData()
            val ids = ArrayList<Long>()
            vehicleIds.split("|").forEach {
                try {
                    ids.add(it.toLong())
                } catch (e: Exception) {

                }
            }
            loadVehicles(ids)
        }
        return vehicles!!
    }

    fun getFilms(filmIds: String): MutableLiveData<List<SwFilm>> {
        Log.d(TAG, "Get films $filmIds")
        if (films == null) {
            films = MutableLiveData()
            val ids = ArrayList<Long>()
            filmIds.split("|").forEach {
                try {
                    ids.add(it.toLong())
                } catch (e: Exception) {

                }
            }
            loadFilms(ids)
        }
        return films!!
    }

    /**
     * Setters
     */
    private fun setNewPlanetData(newPlanet: SwPlanet?) {
        if (planet == null)
            planet = MutableLiveData()

        planet?.value = newPlanet

        if (newPlanet != null)
            SavePlanetInDbTask(db, newPlanet).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private fun setNewVehicleData(resMap: HashMap<Long, SwVehicle>) {
        if (vehicles == null)
            vehicles = MutableLiveData()


        Log.d(TAG, "setNewVehicleData $resMap")

        vehicles?.value = resMap.values.toList()
    }

    private fun setNewFilmData(resMap: HashMap<Long, SwFilm>) {
        if (vehicles == null)
            vehicles = MutableLiveData()

        Log.d(TAG, "setNewFilmData $resMap")

        films?.value = resMap.values.toList()
    }

    /**
     * DB LOADERS
     */
    private fun loadPerson(name: String) {
        LoadPersonFromDbTask(db, name, object : DbResponseListener<SwPerson> {
            override fun onSuccess(result: SwPerson) {
                if (person == null)
                    person = MutableLiveData()

                Log.d(TAG, "Results from DB: $result")
                person?.value = result
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private fun loadPlanet(id: Long) {
        LoadPlanetFromDbTask(db, id, object : DbResponseListener<SwPlanet?> {
            override fun onSuccess(result: SwPlanet?) {
                if (result == null)
                    loadPlanetFromApi(id)
                else
                    setNewPlanetData(result)
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private fun loadVehicles(ids: List<Long>) {
        Log.d(TAG, "loadVehicles $ids")
        val total = ids.size
        var failedCount = 0
        val resMap = HashMap<Long, SwVehicle>()

        ids.forEach { vId ->
            LoadVehicleFromDbTask(db, vId, object : DbResponseListener<SwVehicle?> {
                override fun onSuccess(result: SwVehicle?) {
                    Log.d(TAG, "loadVehicles onSuccess $result")
                    if (result == null) {
                        LoadVehicleFromApiTask(client, vId, object : ApiResponseListener<SwVehicle> {
                            override fun onSuccess(result: SwVehicle) {
                                resMap[vId] = result
                                SaveVehicleInDbTask(db, result).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

                                if (resMap.size == total + failedCount)
                                    setNewVehicleData(resMap)
                            }

                            override fun onFail() {
                                failedCount++
                                if (resMap.size == total + failedCount)
                                    setNewVehicleData(resMap)
                            }
                        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                    } else {
                        resMap[vId] = result

                        if (resMap.size == total + failedCount)
                            setNewVehicleData(resMap)
                    }
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    private fun loadFilms(ids: List<Long>) {
        Log.d(TAG, "loadFilms $ids")
        val total = ids.size
        var failedCount = 0
        val resMap = HashMap<Long, SwFilm>()

        ids.forEach { fId ->
            LoadFilmFromDbTask(db, fId, object : DbResponseListener<SwFilm?> {
                override fun onSuccess(result: SwFilm?) {
                    Log.d(TAG, "loadFilms onSuccess $result")
                    if (result == null) {
                        LoadFilmFromApiTask(client, fId, object : ApiResponseListener<SwFilm> {
                            override fun onSuccess(result: SwFilm) {
                                resMap[fId] = result
                                SaveFilmInDbTask(db, result).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

                                if (resMap.size == total + failedCount)
                                    setNewFilmData(resMap)
                            }

                            override fun onFail() {
                                failedCount++
                                if (resMap.size == total + failedCount)
                                    setNewFilmData(resMap)
                            }
                        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                    } else {
                        resMap[fId] = result

                        if (resMap.size == total + failedCount)
                            setNewFilmData(resMap)
                    }
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }


    /**
     * API LOADERS
     */
    private fun loadPlanetFromApi(id: Long) {
        LoadPlanetFromApiTask(client, id, object : ApiResponseListener<SwPlanet> {
            override fun onSuccess(result: SwPlanet) {
                setNewPlanetData(result)
            }

            override fun onFail() {
                setNewPlanetData(null)
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
}