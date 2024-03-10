package ru.ok.itmo.example

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class NumberFragment : Fragment(R.layout.num_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            view.findViewById<TextView>(R.id.text_number).text = (0..99).random().toString()
        }
    }
}