package com.mijjnapps.swcharacters.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface SwPersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePerson(p: SwPerson)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePersons(vararg p: SwPerson)

    @Query("SELECT * FROM people WHERE name = :name")
    fun getPerson(name: String): SwPerson

    @Query("SELECT * FROM people")
    fun getPersons(): List<SwPerson>

}

@Dao
interface SwPlanetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePlanet(p: SwPlanet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePlanets(vararg p: SwPlanet)

    @Query("SELECT * FROM planets WHERE id = :id")
    fun getPlanet(id: Long): SwPlanet

    @Query("SELECT * FROM planets")
    fun getPlanets(): List<SwPlanet>

}

@Dao
interface SwVehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVehicle(v: SwVehicle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVehicles(vararg v: SwVehicle)

    @Query("SELECT * FROM vehicles WHERE id = :id")
    fun getVehicle(id: Long): SwVehicle

    @Query("SELECT * FROM vehicles")
    fun getVehicles(): List<SwVehicle>

}

@Dao
interface SwFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFilm(v: SwFilm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFilms(vararg v: SwFilm)

    @Query("SELECT * FROM films WHERE id = :id")
    fun getFilm(id: Long): SwFilm

    @Query("SELECT * FROM films")
    fun getFilms(): List<SwFilm>

}