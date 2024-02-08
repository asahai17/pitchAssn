package com.consumers.pitchcatalystassignment.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.consumers.pitchcatalystassignment.R
import com.consumers.pitchcatalystassignment.activities.main.MainActivity
import com.consumers.pitchcatalystassignment.adapters.rvAdapter
import com.consumers.pitchcatalystassignment.model.ListModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListingFragment(private val instance : MainActivity) : Fragment() {
    private lateinit var rv : RecyclerView
    private lateinit var pb : ProgressBar
    private lateinit var fab : FloatingActionButton
    private lateinit var layoutM : LayoutManager
    private lateinit var adapter: rvAdapter
    private val dataList = mutableListOf<ListModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listed,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseViews(view)
        setUpClickListeners()
        setUpDataAndInflateViews()
    }

    private fun setUpClickListeners() {
        fab.setOnClickListener {
            showBottomSheet()
        }
    }

    /**
     * first time setUp of data
     * get data from fireStore, map and pass it to Adapter
     * initially it will only get tasks which are not yet completed
     */
    private fun setUpDataAndInflateViews() {
        dataList.clear()
        instance.firestore.collection(instance.userUUID).whereEqualTo("isCompleted",false).get().addOnCompleteListener {
            if(it.isSuccessful || it.isCanceled || it.isComplete){
                pb.visibility = View.GONE
            }
            val result = it.result
            for(docs in result){
                val listDoc = docs.data
                Log.d("TAG", "setUpDataAndInflateViews: got all data $listDoc" )
                val modelData = ListModel(listDoc["title"].toString(),listDoc["body"].toString()
                    ,listDoc["createdAt"] as Long,listDoc["isCompleted"] as Boolean)


                dataList.add(modelData)
                adapter.setOrUpdateData(dataList,instance.firestore,instance)
            }
        }
    }

    private fun initialiseViews(view: View) {
        pb = view.findViewById(R.id.progressBar)
        rv = view.findViewById(R.id.listingRV)
        fab = view.findViewById(R.id.fab)
        layoutM = LinearLayoutManager(requireContext())
        adapter = rvAdapter()
        rv.layoutManager = layoutM
        rv.adapter = adapter
        adapter.setOrUpdateData(mutableListOf(),instance.firestore,instance)
    }
    private fun showBottomSheet() {
        val bottomSheet = bottomSheet()
        bottomSheet.setBottomSheetListener(object : bottomSheet.BottomSheetListener {
            override fun onConfirmButtonClicked(title: String, body: String) {
                val createdTime = System.currentTimeMillis()

                val listModel = ListModel(title, body, createdTime, false)
                dataList.add(listModel)
                adapter.setOrUpdateData(dataList,instance.firestore,instance)
                val mapToSend = mutableMapOf(
                    "title" to title,
                    "body" to body,
                    "createdAt" to createdTime,
                    "isCompleted" to false
                )
                instance.firestore.collection(instance.userUUID).document(createdTime.toString())
                    .set(mapToSend).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(instance, "Task added", Toast.LENGTH_SHORT).show()
                        } else
                            Toast.makeText(instance, "Task not added", Toast.LENGTH_SHORT).show()
                    }
            }
        })
        bottomSheet.show(instance.manager, "BottomSheetTask")
    }
}