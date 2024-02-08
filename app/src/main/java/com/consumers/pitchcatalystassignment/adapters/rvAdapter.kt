package com.consumers.pitchcatalystassignment.adapters

import android.graphics.Paint
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.consumers.pitchcatalystassignment.R
import com.consumers.pitchcatalystassignment.activities.main.MainActivity
import com.consumers.pitchcatalystassignment.fragments.bottomSheet
import com.consumers.pitchcatalystassignment.model.ListModel
import com.google.firebase.firestore.FirebaseFirestore

class rvAdapter : RecyclerView.Adapter<rvAdapter.holder>() {
    private var dataLists = mutableListOf<ListModel>()
    private lateinit var instance: MainActivity
    private lateinit var firestore: FirebaseFirestore
    private var VIEW_CELL = R.layout.cardview_listitem

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.titleList)
        val body = itemView.findViewById<TextView>(R.id.bodyList)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkboxList)
    }

    fun setOrUpdateData(
        mutableList: MutableList<ListModel>,
        firestore: FirebaseFirestore, mainActivity: MainActivity
    ) {
        dataLists = mutableList
        this.firestore = firestore
        instance = mainActivity
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val itemViewToShow: View =
            LayoutInflater.from(parent.context).inflate(VIEW_CELL, parent, false)

        return holder(itemViewToShow)
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        Log.d("TAG", "onBindViewHolder: called and added")

        holder.title.text =
            dataLists[position].title.toString()
        if (dataLists[position].isCompleted) {
            holder.title.paintFlags =
                holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.checkBox.isChecked = true
        }else{
            holder.checkBox.isChecked = false
        }
        holder.body.text =
            dataLists[position].body.toString()

        holder.checkBox.setOnClickListener {
            if(holder.checkBox.isChecked){
                completeTask(dataLists[position].createdAt.toString(), true)
            }else{
                completeTask(dataLists[position].createdAt.toString(), false)
            }
            dataLists.removeAt(position)
            notifyDataSetChanged()
        }

    }

    /**
     * when [isCompleted] is true then task is added to completedList
     * when [isCompleted] is false then task is added to pending
     */
    private fun completeTask(createdAt: String, isCompleted: Boolean) {
        if (isCompleted) {
            instance.firestore.collection(instance.userUUID).document(createdAt)
                .update("isCompleted", true).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(instance, "Task completed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(instance, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            instance.firestore.collection(instance.userUUID).document(createdAt)
                .update("isCompleted", false).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(instance, "Operation Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(instance, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}