package com.mijjnapps.swcharacters.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [SwPerson::class, SwPlanet::class, SwVehicle::class, SwFilm::class], version = 1)
abstract class SwDatabase : RoomDatabase() {
    abstract fun personsDao(): SwPersonDao
    abstract fun planetDao(): SwPlanetDao
    abstract fun vehicleDao(): SwVehicleDao
    abstract fun filmDao(): SwFilmDao
}