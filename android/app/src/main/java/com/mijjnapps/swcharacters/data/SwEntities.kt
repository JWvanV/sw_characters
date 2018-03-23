package com.mijjnapps.swcharacters.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "people")
data class SwPerson(
        @PrimaryKey
        var name: String = "",

        @ColumnInfo(name = "birth_year")
        var birthYear: String = "",

        @ColumnInfo(name = "ids_film")
        var filmIds: String = "",

        @ColumnInfo(name = "ids_vehicle")
        var vehicleIds: String = "",

        @ColumnInfo(name = "id_home_world")
        var homeWorldId: Int = -1
)

@Entity(tableName = "vehicles")
data class SwVerhicle(
        @PrimaryKey(autoGenerate = true) // true for autoincrement, false als ik dat zelf doe
        val id: Long,
        @ColumnInfo(name = "vehicle_class")
        val verhicleClass: String = "",

        @Embedded // aw yeah
        val driver: SwPerson
)