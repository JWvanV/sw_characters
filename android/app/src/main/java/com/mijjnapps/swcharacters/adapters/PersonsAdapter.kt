package com.mijjnapps.swcharacters.adapters

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.mijjnapps.swcharacters.R
import com.mijjnapps.swcharacters.data.SwPerson
import com.mijjnapps.swcharacters.utils.PrefsUtils
import com.mijjnapps.swcharacters.utils.inflate
import kotlinx.android.synthetic.main.item_person.view.*


class PersonsAdapter(val listener: OnPersonSelectionListener) : RecyclerView.Adapter<PersonsAdapter.ViewHolder>() {

    interface OnPersonSelectionListener {
        fun onPersonSelected(p: SwPerson)
    }

    private val persons: ArrayList<SwPerson> = ArrayList()

    override fun getItemCount(): Int {
        return persons.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val clicker: View = v
        var name: TextView = v.personName
        var birthYear: TextView = v.personBirthyear
        var favorite: ImageButton = v.personFavorite

        fun setIsFavorite(isFavorite: Boolean) {
            favorite.setImageResource(
                    if (isFavorite)
                        R.drawable.ic_star_full
                    else
                        R.drawable.ic_star_border)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.item_person))

    override fun onBindViewHolder(h: ViewHolder, position: Int) {
        val context = h.name.context
        val p = persons[position]
        h.name.text = p.name
        h.birthYear.text = p.birthYear
        h.setIsFavorite(PrefsUtils.isFavorite(context, p.name))

        h.clicker.setOnClickListener { listener.onPersonSelected(p) }
        h.favorite.setOnClickListener {
            val isFavorite = PrefsUtils.toggleFavorite(context, p.name)
            h.setIsFavorite(isFavorite)
            sortPersons(context)
        }
    }

    //List modification
    fun updateData(c: Context?, newPersons: List<SwPerson>) {
        persons.clear()
        persons.addAll(newPersons)
        sortPersons(c)
    }

    fun sortPersons(c: Context?) {
        val sortType = PrefsUtils.getSortType(c)
//        val oldPersons = ArrayList(persons)
        persons.sortBy {
            (if (PrefsUtils.isFavorite(c, it.name)) "0" else "1") +
                    when (sortType) {
                        PrefsUtils.SortType.NAME -> it.name
                        PrefsUtils.SortType.BIRTHYEAR -> it.birthYear
                    }
        }

//        try {
//            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
//                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
//                        oldItemPosition < oldPersons.size
//                                && newItemPosition < persons.size
//                                && oldPersons[oldItemPosition].name == persons[newItemPosition].name
//
//                override fun getOldListSize() = oldPersons.size
//
//                override fun getNewListSize() = persons.size
//
//                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
//                        oldItemPosition < oldPersons.size
//                                && newItemPosition < persons.size
//                                && oldPersons[oldItemPosition] == persons[newItemPosition]
//            })
//            diffResult.dispatchUpdatesTo(this)
//        } catch (e: Exception) {
//        }
        notifyDataSetChanged()
    }
}