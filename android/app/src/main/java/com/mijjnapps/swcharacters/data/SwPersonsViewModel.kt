package com.mijjnapps.swcharacters.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import com.github.swapi4j.SwapiClient
import com.mijjnapps.swcharacters.tasks.*

class SwPersonsViewModel : ViewModel() {

    data class PersonsData(val persons: List<SwPerson>)

    internal val client = SwapiClient()
    private lateinit var db: SwDatabase
    private var persons: MutableLiveData<PersonsData>? = null

    private var personsLoadingDbTask: LoadPersonsFromDbTask? = null
    private var personRefreshingTask: RefreshAllPersonsTask? = null

    fun init(applicationContext: Context) {
        db = Room.databaseBuilder(
                applicationContext,
                SwDatabase::class.java,
                "swCharactersDb.db")
                .build()
    }

    fun getKnownPersons(runOnFail: Runnable): MutableLiveData<PersonsData>? {
        if (persons == null) {
            persons = MutableLiveData()
            loadPersonsFromDb(Runnable { updatePersonsFromApi(runOnFail) })
        }
        return persons
    }

    private fun setNewPersonsData(newPersons: List<SwPerson>) {
        if (persons == null)
            persons = MutableLiveData()

        persons?.value = PersonsData(newPersons)

        SavePersonsInDbTask(db, newPersons).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private fun loadPersonsFromDb(runOnComplete: Runnable) {
        if (personsLoadingDbTask == null) {
            personsLoadingDbTask = LoadPersonsFromDbTask(db, object : DbListResponseListener<SwPerson> {
                override fun onSuccess(result: List<SwPerson>) {
                    personsLoadingDbTask = null
                    if (result.isNotEmpty())
                        setNewPersonsData(result)
                    runOnComplete.run()
                }
            })
            personsLoadingDbTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    private fun updatePersonsFromApi(runOnFail: Runnable) {
        if (personRefreshingTask == null) {
            personRefreshingTask = RefreshAllPersonsTask(client, object : ApiListResponseListener<SwPerson> {
                override fun onSuccess(result: List<SwPerson>) {
                    personRefreshingTask = null
                    setNewPersonsData(result)
                }

                override fun onFail() {
                    personRefreshingTask = null
                    runOnFail.run()
                }
            })
            personRefreshingTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

//    fun loadNewPersonPage(c: Context, runOnFailed: Runnable) {
//        if (personPageLoadingTask == null) {
//            val newPageNumber = PrefsUtils.getPersonPagesLoaded(c) + 1
//            personPageLoadingTask = PersonPageTask(client, newPageNumber, object : PersonPageTask.PageResponseListener {
//                override fun onPageSuccess(result: Page<Person>) {
//                    PrefsUtils.setTotalPersons(c, result.count)
//                    val newPersons = result.results
//                    if (newPersons.isEmpty()) {
//
//                    } else {
//
//                    }
//                }
//
//                override fun onPageFailed() {
//                    runOnFailed.run()
//                }
//            })
//        }
//    }
}