package com.consumers.pitchcatalystassignment.activities.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import com.consumers.pitchcatalystassignment.R
import com.consumers.pitchcatalystassignment.activities.base.BaseActivity
import com.consumers.pitchcatalystassignment.fragments.CompletedTasksFragment
import com.consumers.pitchcatalystassignment.fragments.ListingFragment

class MainActivity : BaseActivity() {
    private lateinit var container: FragmentContainerView
    private lateinit var completedTaskBtn: ImageButton
    private lateinit var exitBtn: ImageButton
    private lateinit var textTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialiseViews()
        setUpClickListeners()
        manager.beginTransaction().replace(container.id, ListingFragment(this)).commit()
        Log.d("TAG", "onCreate: uuid $userUUID")
    }

    private fun setUpClickListeners() {
        completedTaskBtn.setOnClickListener {
            manager.beginTransaction().replace(container.id, CompletedTasksFragment(this))
                .addToBackStack(null).commit()
            exitBtn.visibility = View.VISIBLE
            completedTaskBtn.visibility = View.GONE
            textTV.text = "Completed"
        }

        exitBtn.setOnClickListener {
            manager.popBackStack()
            exitBtn.visibility = View.GONE
            completedTaskBtn.visibility = View.VISIBLE
            textTV.text = "Tasks"
        }
    }

    private fun initialiseViews() {
        container = findViewById(R.id.container)
        exitBtn = findViewById(R.id.backExitBtn)
        textTV = findViewById(R.id.tvToolbar)
        completedTaskBtn = findViewById(R.id.completedTaskListBtn)
    }
}