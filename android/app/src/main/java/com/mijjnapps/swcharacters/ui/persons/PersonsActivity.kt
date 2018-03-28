package com.mijjnapps.swcharacters.ui.persons

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mijjnapps.swcharacters.R
import kotlinx.android.synthetic.main.activity_persons.*


class PersonsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persons)
        setSupportActionBar(toolbar)
    }
}
