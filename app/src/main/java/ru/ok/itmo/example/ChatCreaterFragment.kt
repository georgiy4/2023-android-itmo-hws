package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class ChatCreaterFragment:  Fragment(R.layout.fragment_chat_creater){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.bt_back).setOnClickListener {
            requireActivity().onBackPressed()
        }

        val textName = view.findViewById<EditText>(R.id.channel_name)
        val textMessage = view.findViewById<EditText>(R.id.text_message)

        view.findViewById<Button>(R.id.bt_create).setOnClickListener {
            parentFragmentManager.setFragmentResult("create_chat", bundleOf(
                Pair("chat_name", textName.text.toString()),
                Pair("message", textMessage.text.toString())
            ))
            requireActivity().onBackPressed()
        }
    }
}