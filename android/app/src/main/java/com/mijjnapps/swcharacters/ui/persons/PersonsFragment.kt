package com.mijjnapps.swcharacters.ui.persons

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.mijjnapps.swcharacters.R
import com.mijjnapps.swcharacters.adapters.PersonsAdapter
import com.mijjnapps.swcharacters.data.SwPerson
import com.mijjnapps.swcharacters.data.SwPersonsViewModel
import com.mijjnapps.swcharacters.tasks.ApiListResponseListener
import com.mijjnapps.swcharacters.tasks.QueryCharacterTask
import com.mijjnapps.swcharacters.ui.person.PersonActivity
import com.mijjnapps.swcharacters.utils.PrefsUtils
import kotlinx.android.synthetic.main.fragment_persons.*

class PersonsFragment : Fragment(), PersonsAdapter.OnPersonSelectionListener {

    companion object {
        private const val TAG = "PersonsFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_persons, container, false)

    enum class ViewState {
        LOADING, ERROR, EMPTY, CONTENT
    }

    private lateinit var data: SwPersonsViewModel
    private var currentState: ViewState = ViewState.LOADING
    private var query: String = ""

    private var sortItem: MenuItem? = null
    private var searchItem: MenuItem? = null

    private var regularPersonsData: SwPersonsViewModel.PersonsData? = null
    private var queryPersonsData: SwPersonsViewModel.PersonsData? = null

    private lateinit var adapter: PersonsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = ViewModelProviders.of(this).get(SwPersonsViewModel::class.java)
        adapter = PersonsAdapter(this)
        personsContent.adapter = adapter

        if (savedInstanceState == null) {
            setUiState(ViewState.LOADING)
            data.init(view.context)
        } else {
            //TODO get state and query from savedInstanceState
        }

        data.getKnownPersons(Runnable {
            if (regularPersonsData?.persons?.isEmpty() != false)
                setUiState(ViewState.ERROR)
        })?.observe(this, Observer<SwPersonsViewModel.PersonsData> { updateUiFrom(it) })
    }

    private fun setUiState(newState: ViewState) {
        this.currentState = newState

        if (personsRoot != null)
            TransitionManager.beginDelayedTransition(personsRoot)

        val fromQuery = query.isNotBlank()

        personsLoading.visibility = if (currentState == ViewState.LOADING) View.VISIBLE else View.GONE
        personsError.visibility = if (currentState == ViewState.ERROR) View.VISIBLE else View.GONE
        personsEmpty.visibility = if (currentState == ViewState.EMPTY) View.VISIBLE else View.GONE
        personsContent.visibility = if (currentState == ViewState.CONTENT) View.VISIBLE else View.GONE

//        when (currentState) {
//            ViewState.LOADING -> {
//
//            }
//            ViewState.ERROR -> {
//
//            }
//            ViewState.EMPTY -> {
//
//            }
//            ViewState.CONTENT -> {
//
//            }
//        }
    }

    private fun updateUiFrom(data: SwPersonsViewModel.PersonsData?) {
        if (data == null) {
            setUiState(ViewState.ERROR)
        } else {
            if (query.isNotBlank()) queryPersonsData = data
            else regularPersonsData = data

            if (data.persons.isEmpty()) {
                setUiState(ViewState.EMPTY)
            } else {
                adapter.updateData(context, data.persons)
                setUiState(ViewState.CONTENT)
            }
        }
        updateMenuItems()
    }

    private fun updateMenuItems() {
        val visible = currentState == ViewState.CONTENT && regularPersonsData?.persons?.isNotEmpty() == true

        sortItem?.isVisible = visible
        searchItem?.isVisible = visible
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_persons, menu)

        sortItem = menu.findItem(R.id.action_sort)
        searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView?

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                showRegularData()
                return true
            }
        })
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var currentSearchTask: QueryCharacterTask? = null

            override fun onQueryTextSubmit(q: String): Boolean {
                if (!searchView.isIconified)
                    searchView.isIconified = true
                searchItem?.collapseActionView()
                showRegularData()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                query = s
                if (s.length > 1)
                    searchView.postDelayed({
                        if (query == s) {
                            Log.d(TAG, "$query == $s")
                            currentSearchTask?.cancel(true)
                            currentSearchTask = QueryCharacterTask(data.client, s.trim(), object : ApiListResponseListener<SwPerson> {
                                override fun onSuccess(result: List<SwPerson>) {
                                    Log.d(TAG, "Response: ${result.size}")
                                    updateUiFrom(SwPersonsViewModel.PersonsData(result))
                                }

                                override fun onFail() {
                                    setUiState(ViewState.ERROR)
                                }
                            })

                            setUiState(ViewState.LOADING)
                            currentSearchTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                        } else {
                            Log.d(TAG, "$query != $s")
                        }
                    }, 200)
                return false
            }
        })
        updateMenuItems()
    }

    private fun showRegularData() {
        query = ""
        updateUiFrom(regularPersonsData)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                PrefsUtils.setSortType(context, PrefsUtils.SortType.NAME)
                adapter.sortPersons(context)
                true
            }
            R.id.action_sort_birthyear -> {
                PrefsUtils.setSortType(context, PrefsUtils.SortType.BIRTHYEAR)
                adapter.sortPersons(context)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onPersonSelected(p: SwPerson) {
        PersonActivity.show(this, p.name)
    }
}
