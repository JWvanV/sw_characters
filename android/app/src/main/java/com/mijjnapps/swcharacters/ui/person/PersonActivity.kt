package com.mijjnapps.swcharacters.ui.person

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.mijjnapps.swcharacters.R
import com.mijjnapps.swcharacters.ui.persons.PersonsActivity
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {

    companion object {
        const val KEY_NAME = "key_name"

        fun show(f: Fragment, personName: String) {
            val i = Intent(f.context, PersonActivity::class.java)
            i.putExtra(KEY_NAME, personName)
            f.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
