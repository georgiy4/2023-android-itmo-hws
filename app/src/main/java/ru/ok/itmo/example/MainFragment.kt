package ru.ok.itmo.example

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class MainFragment : Fragment(R.layout.fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.bt_start).setOnClickListener {
            parentFragmentManager.setFragmentResult("start", bundleOf())
        }
    }
}