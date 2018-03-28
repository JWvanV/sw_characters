package com.mijjnapps.swcharacters.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.Room
import android.content.Context
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
            SavePlanetInDbTask(db, newPlanet).execute()
    }

    private fun setNewVehicleData(resMap: HashMap<Long, SwVehicle>) {
        if (vehicles == null)
            vehicles = MutableLiveData()

        vehicles?.value = resMap.values.toList()
    }

    private fun setNewFilmData(resMap: HashMap<Long, SwFilm>) {
        if (vehicles == null)
            vehicles = MutableLiveData()

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
        }).execute()
    }

    private fun loadPlanet(id: Long) {
        LoadPlanetFromDbTask(db, id, object : DbResponseListener<SwPlanet?> {
            override fun onSuccess(result: SwPlanet?) {
                if (result == null)
                    loadPlanetFromApi(id)
                else
                    setNewPlanetData(result)
            }
        }).execute()
    }

    private fun loadVehicles(ids: List<Long>) {
        val total = ids.size
        var failedCount = 0
        val resMap = HashMap<Long, SwVehicle>()

        ids.forEach { vId ->
            LoadVehicleFromDbTask(db, vId, object : DbResponseListener<SwVehicle?> {
                override fun onSuccess(result: SwVehicle?) {
                    if (result == null) {
                        LoadVehicleFromApiTask(client, vId, object : ApiResponseListener<SwVehicle> {
                            override fun onSuccess(result: SwVehicle) {
                                resMap[vId] = result
                                SaveVehicleInDbTask(db, result).execute()

                                if (resMap.size == total + failedCount)
                                    setNewVehicleData(resMap)
                            }

                            override fun onFail() {
                                failedCount++
                            }
                        }).execute()
                    } else {
                        resMap[vId] = result

                        if (resMap.size == total + failedCount)
                            setNewVehicleData(resMap)
                    }
                }
            }).execute()
        }
    }

    private fun loadFilms(ids: List<Long>) {
        val total = ids.size
        var failedCount = 0
        val resMap = HashMap<Long, SwFilm>()

        ids.forEach { fId ->
            LoadFilmFromDbTask(db, fId, object : DbResponseListener<SwFilm?> {
                override fun onSuccess(result: SwFilm?) {
                    if (result == null) {
                        LoadFilmFromApiTask(client, fId, object : ApiResponseListener<SwFilm> {
                            override fun onSuccess(result: SwFilm) {
                                resMap[fId] = result
                                SaveFilmInDbTask(db, result).execute()

                                if (resMap.size == total + failedCount)
                                    setNewFilmData(resMap)
                            }

                            override fun onFail() {
                                failedCount++
                            }
                        }).execute()
                    } else {
                        resMap[fId] = result

                        if (resMap.size == total + failedCount)
                            setNewFilmData(resMap)
                    }
                }
            }).execute()
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
        }).execute()
    }
}