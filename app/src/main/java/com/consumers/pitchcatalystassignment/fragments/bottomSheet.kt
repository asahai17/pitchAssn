package com.consumers.pitchcatalystassignment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.consumers.pitchcatalystassignment.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class bottomSheet : BottomSheetDialogFragment() {
    interface BottomSheetListener {
        fun onConfirmButtonClicked(title: String, body: String)
    }
    private var bottomSheetListener: BottomSheetListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottomsheet,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addTask : Button = view.findViewById(R.id.addTaskBS)
        val title : EditText = view.findViewById(R.id.titleInputBS)
        val body : EditText = view.findViewById(R.id.bodyInputBS)

        addTask.setOnClickListener {
            if(title.length() == 0){
                title.requestFocus()
                title.error = "Field can't be empty"
                return@setOnClickListener
            }

            bottomSheetListener?.onConfirmButtonClicked(title.text.toString(),body.text.toString())
            dismiss()
        }
    }
    fun setBottomSheetListener(listener: BottomSheetListener) {
        this.bottomSheetListener = listener
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}