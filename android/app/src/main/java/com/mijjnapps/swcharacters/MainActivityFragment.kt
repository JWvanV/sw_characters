package com.mijjnapps.swcharacters

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.swapi4j.SwapiClient

class MainActivityFragment : Fragment() {

    companion object {
        private const val TAG = "MainActivityFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Task().execute()
    }

    internal class Task : AsyncTask<Unit, Unit, Unit>(){
        override fun doInBackground(vararg params: Unit?) {

            // create the client
            val client = SwapiClient()

//            // get the first page of planets
//            val firstPlanets = client.planets
//
//            // get a specific page of planets
//            val thirdPlanets = client.getPlanets(3)
//
//            // get a planet by id
//            val planet1 = client.getPlanetById(1)
//
//            // get all planets
//            val allPlanets = client.allPlanets

            val then = System.currentTimeMillis()
            Log.i(TAG, "Starting loading all persons")
            try {
//                val allCharacter = client.allPersons
//                Log.i(TAG, "${allCharacter?.size}")
            }catch(e:Exception){
                Log.e(TAG, "Person loading error: ", e)
            }
            Log.i(TAG, "Done loading all persons ${System.currentTimeMillis() - then}ms")

//            Log.d(TAG, "firstPlanets $firstPlanets")
//            Log.d(TAG, "thirdPlanets $thirdPlanets")
//            Log.d(TAG, "planet1 $planet1")
//            Log.d(TAG, "allPlanets $allPlanets")
        }
    }
}
