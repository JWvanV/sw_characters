package com.mijjnapps.swcharacters.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface SwPersonDao {

//    Create
//    Read
//    Update
//    Delete

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePerson(p: SwPerson)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePersons(vararg p: SwPerson)

    @Query("SELECT * FROM people")
    fun getAvailablePersons(): List<SwPerson>

//    @Query("")
//    fun getPersonWithId(name: String)

}