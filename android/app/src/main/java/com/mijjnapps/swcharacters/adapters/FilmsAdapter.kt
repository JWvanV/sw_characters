package com.mijjnapps.swcharacters.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mijjnapps.swcharacters.R
import com.mijjnapps.swcharacters.data.SwFilm
import com.mijjnapps.swcharacters.utils.inflate
import kotlinx.android.synthetic.main.item_film.view.*


class FilmsAdapter : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    private val films: ArrayList<SwFilm> = ArrayList()

    override fun getItemCount(): Int {
        return films.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.filmName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.item_film))

    override fun onBindViewHolder(h: ViewHolder, position: Int) {
        val v = films[position]
        h.name.text = v.name
    }

    fun updateData(newFilms: List<SwFilm>) {
        films.clear()
        films.addAll(newFilms)
        notifyDataSetChanged()
    }
}