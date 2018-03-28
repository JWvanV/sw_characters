package com.mijjnapps.swcharacters.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.github.swapi4j.model.Film
import com.github.swapi4j.model.Person
import com.github.swapi4j.model.Planet
import com.github.swapi4j.model.Vehicle

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
        var homeWorldId: Long = -1) {

    constructor(p: Person) : this(p.name, p.birthYear, p.getFilmIdString(), p.getVehicleIdString(), p.getHomeWorldId())
}

private fun Person.getFilmIdString() = films.map { it.id }.joinToString("|")
private fun Person.getVehicleIdString() = vehicles.map { it.id }.joinToString("|")
private fun Person.getHomeWorldId() = homeworld.id


@Entity(tableName = "planets")
data class SwPlanet(
        @PrimaryKey(autoGenerate = false) // true for autoincrement, false als ik dat zelf doe
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String = "") {

        constructor(p: Planet) : this(p.id, p.name)
}

@Entity(tableName = "vehicles")
data class SwVehicle(
        @PrimaryKey(autoGenerate = false) // true for autoincrement, false als ik dat zelf doe
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String = "") {

        constructor(v: Vehicle) : this(v.id, v.name)
}

@Entity(tableName = "films")
data class SwFilm(
        @PrimaryKey(autoGenerate = false) // true for autoincrement, false als ik dat zelf doe
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String = "") {

        constructor(f: Film) : this(f.id, f.title)
}