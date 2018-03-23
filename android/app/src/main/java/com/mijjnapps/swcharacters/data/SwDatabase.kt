package com.mijjnapps.swcharacters.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [SwPerson::class, SwVerhicle::class], version = 1)
abstract class SwDatabase : RoomDatabase() {
    abstract fun pDao(): SwPersonDao
}