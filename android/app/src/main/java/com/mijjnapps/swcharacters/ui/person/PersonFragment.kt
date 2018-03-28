package com.mijjnapps.swcharacters.ui.person

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mijjnapps.swcharacters.R
import com.mijjnapps.swcharacters.adapters.FilmsAdapter
import com.mijjnapps.swcharacters.adapters.VehiclesAdapter
import com.mijjnapps.swcharacters.data.*
import kotlinx.android.synthetic.main.fragment_person.*

class PersonFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_person, container, false)

    enum class ViewState {
        LOADING, CONTENT
    }

    private lateinit var data: SwPersonViewModel
    private var currentState: ViewState = ViewState.LOADING

    private lateinit var vehicleAdapter: VehiclesAdapter
    private lateinit var filmAdapter: FilmsAdapter

    private var person: SwPerson? = null
    private var planet: SwPlanet? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pName = activity?.intent?.getStringExtra(PersonActivity.KEY_NAME)

        Log.d("PersonFragment", "name $pName")

        if (pName.isNullOrBlank())
            activity?.finish()
        else {
            data = ViewModelProviders.of(this).get(SwPersonViewModel::class.java)
            vehicleAdapter = VehiclesAdapter()
            filmAdapter = FilmsAdapter()

            personVehicles.adapter = vehicleAdapter
            personFilms.adapter = filmAdapter

            if (savedInstanceState == null) {
                data.init(view.context)
            }

            data.getPerson(pName!!).observe(this, Observer { setUiFromPerson(it) })

            setLoading(true)
        }
    }

    private fun setLoading(loading: Boolean) {
        personLoading.visibility = if (loading) View.VISIBLE else View.GONE
        personContent.visibility = if (loading) View.GONE else View.VISIBLE
    }

    private fun setUiFromPerson(p: SwPerson?) {
        if (p == null) {
            activity?.finish()
        } else {
            person = p

            personName.text = p.name
            personBirthYear.text = p.birthYear

            data.getPlanet(p.homeWorldId).observe(this, Observer { updateUiFromHomeWorld(it) })

            data.getVehicles(p.vehicleIds).observe(this, Observer { updateUiFromVehicles(it) })
            data.getFilms(p.filmIds).observe(this, Observer { updateUiFromFilms(it) })

            setLoading(false)
        }
    }

    private fun updateUiFromHomeWorld(p: SwPlanet?) {
        planet = p
        if (p == null) {
            personHomeWorldName.text = getString(R.string.unknown)
        } else {
            personHomeWorldName.text = p.name
        }
    }

    private fun updateUiFromVehicles(v: List<SwVehicle>?) {
        if (v == null) {
        } else {
            vehicleAdapter.updateData(v)
        }
    }

    private fun updateUiFromFilms(f: List<SwFilm>?) {
        if (f == null) {
        } else {
            filmAdapter.updateData(f)
        }
    }
}
