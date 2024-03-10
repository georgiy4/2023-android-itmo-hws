package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import retrofit2.HttpException

class AuthorizationFragment : Fragment(R.layout.authorization_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text_password = view.findViewById<EditText>(R.id.text_password)
        val text_login = view.findViewById<EditText>(R.id.text_login)
        val button = view.findViewById<Button>(R.id.bt_enter)
        text_password.addTextChangedListener {
            button.isEnabled = !(text_password.text.toString() == "" || text_login.text.toString() == "")
        }
        text_login.addTextChangedListener {
            button.isEnabled = !(text_password.text.toString() == "" || text_login.text.toString() == "")
        }
        button.setOnClickListener {
            var token: String? = null

            val dataAccount = DataAccount(
                text_login.text.toString(),
                text_password.text.toString()
            )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    token = MainFragment.api.login(dataAccount).string()
                    withContext(Dispatchers.Main) {
                        parentFragmentManager.setFragmentResult("enter", bundleOf(
                            Pair("token", token),
                            Pair("userName", dataAccount.name)
                        ))
                    }
                } catch (e: HttpException) {
                    e.code()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            getHttpMessage(e),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            text_password.setText("")
            text_login.setText("")
        }

    }

    private fun getHttpMessage(e: HttpException) : String{
        return when (e.code()) {
            401 -> "Incorrect password or login"
            else -> e.toString()
        }
    }
}