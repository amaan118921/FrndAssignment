package com.example.frndassignment.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.frndassignment.R
import com.example.frndassignment.databinding.FragmentAddNewTaskBinding
import com.example.frndassignment.domain.servermodels.TaskDetail
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.utils.empty
import com.example.frndassignment.utils.throttleClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTaskBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddNewTaskBinding

    private var onDoneClick: ((TaskModel) -> Unit)? = null

    fun setOnDoneClick(onDoneClick: ((TaskModel) -> Unit)) {
        this.onDoneClick = onDoneClick
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tilTitle.editText?.requestFocus()
            ivClose.throttleClick {
                dismiss()
            }
            btnAdd.throttleClick {
                val title = binding.tilTitle.editText?.text?.toString() ?: String.empty()
                val desc = binding.tilDesc.editText?.text?.toString() ?: String.empty()
                if (title.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.title_cannot_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@throttleClick
                }
                onDoneClick?.invoke(
                    TaskModel(
                        taskDetail = TaskDetail(
                            title = title,
                            description = desc
                        )
                    )
                )
                dismiss()
            }
        }
    }
}