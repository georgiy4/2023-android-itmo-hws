package ru.ok.itmo.example;

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

class FragmentProgressBar(private val work: ((ProgressBar) -> Unit)) : Fragment(R.layout.fragment_progress_bar) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener{
            work(view.findViewById(R.id.progress_bar))
        }
    }
}