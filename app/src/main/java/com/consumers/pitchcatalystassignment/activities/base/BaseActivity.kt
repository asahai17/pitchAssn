package com.consumers.pitchcatalystassignment.activities.base

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.consumers.pitchcatalystassignment.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import java.util.prefs.Preferences

open class BaseActivity : AppCompatActivity() {
    lateinit var firestore : FirebaseFirestore
    lateinit var manager : FragmentManager
    lateinit var userUUID: String
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setUpAndInitialise()
    }

    /**
     * Initially setting up required firestore and uuid
     * we will use only one uuid for one user in firebase
     * and one instance of firebase through whole app
     */
    private fun setUpAndInitialise() {
        manager = supportFragmentManager
        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("AuthDetails", MODE_PRIVATE)
        if(sharedPreferences.contains("uuid"))
            userUUID = sharedPreferences.getString("uuid","").toString()
        else{
            userUUID = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("uuid",userUUID).apply()
        }
    }
}