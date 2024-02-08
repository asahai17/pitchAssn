package com.consumers.pitchcatalystassignment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.consumers.pitchcatalystassignment.R
import com.consumers.pitchcatalystassignment.activities.main.MainActivity
import com.consumers.pitchcatalystassignment.adapters.rvAdapter
import com.consumers.pitchcatalystassignment.model.ListModel

class CompletedTasksFragment(private val instance : MainActivity) : Fragment() {
    private lateinit var rv : RecyclerView
    private lateinit var adapter: rvAdapter
    private lateinit var layoutManager: LayoutManager
    private val dataList = mutableListOf<ListModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed_tasks,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseView(view)

        setUpDataAndInflateViews()
    }

    private fun initialiseView(view: View) {
        rv = view.findViewById(R.id.completedRV)
        layoutManager = LinearLayoutManager(requireContext())
        rv.layoutManager = layoutManager
        adapter = rvAdapter()
        rv.adapter = adapter
    }
    /**
     * first time setUp of data
     * get data from fireStore, map and pass it to Adapter
     * initially it will only get tasks which are not yet completed
     */
    private fun setUpDataAndInflateViews() {
        dataList.clear()
        instance.firestore.collection(instance.userUUID).whereEqualTo("isCompleted",true).get().addOnCompleteListener {
            val result = it.result

            for(docs in result){
                val listDoc = docs.data
                val modelData = ListModel(listDoc["title"].toString(),listDoc["body"].toString()
                    ,listDoc["createdAt"] as Long,listDoc["isCompleted"] as Boolean)


                dataList.add(modelData)
                adapter.setOrUpdateData(dataList,instance.firestore,instance)
            }
        }
    }
}