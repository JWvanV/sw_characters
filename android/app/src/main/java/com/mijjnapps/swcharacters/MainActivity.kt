package com.mijjnapps.swcharacters

import android.arch.persistence.room.Room
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.github.swapi4j.SwapiClient
import com.github.swapi4j.model.Person
import com.mijjnapps.swcharacters.data.SwDatabase
import com.mijjnapps.swcharacters.data.SwPerson
import com.mijjnapps.swcharacters.tasks.QueryCharacterTask
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val client = SwapiClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        object: AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit): Unit? {

                val db = Room.databaseBuilder(
                        applicationContext,
                        SwDatabase::class.java,
                        "swCharactersDb.db")
                        .build()

                val p = SwPerson()
                p.name = "someOtherName"
                p.birthYear = "12334sfg"
                p.homeWorldId = 1
                p.filmIds = "1|2|3|4"
                p.vehicleIds = "1|2|3|4|5"
                db.pDao().savePerson(p)

                db.pDao().getAvailablePersons().forEach {
                    Log.d(TAG, "Person: $it")
                }

                return null
            }

        }.execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView?
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var searchTime = 0L
            var currentSearchTask: QueryCharacterTask? = null

            override fun onQueryTextSubmit(query: String): Boolean {
                // Toast like print
//                UserFeedback.show("SearchOnQueryTextSubmit: $query")
                if (!searchView.isIconified)
                    searchView.isIconified = true
                searchItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                searchTime = System.currentTimeMillis() + 800
                searchView.postDelayed({
                    if (System.currentTimeMillis() > searchTime) {
                        currentSearchTask?.cancel(true)
                        currentSearchTask = QueryCharacterTask(client, s.trim(), object : QueryCharacterTask.ResponseListener {
                            override fun onQuerySuccess(result: List<Person>) {
                                if (result.isEmpty()) {
                                    //TODO set ui_state to no_results
                                } else {
                                    //TODO set ui_state to results(with query)
                                }
                            }

                            override fun onQueryFailed() {
                                //TODO set ui_state to query_error
                            }
                        })
                        //TODO set ui_state to loading
                        currentSearchTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                    }
                }, 1000)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
