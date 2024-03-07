package ru.ok.itmo.example

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(R.layout.activity) {

    companion object {
        const val correctLogin = "geor"
        const val correctPassword = "123456"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = findViewById<Button>(R.id.button)
        val viewLogin = findViewById<EditText>(R.id.text_login)
        val viewPassword = findViewById<EditText>(R.id.text_password)

        viewPassword.setOnEditorActionListener { _, _, _ ->
            button.callOnClick()
            false
        }

        button?.setOnClickListener {
            val login = viewLogin.text.toString()
            val password = viewPassword.text.toString()
            if (login == "") {
                showMessage(getString(R.string.no_login))
            } else if (password == ""){
                showMessage(getString(R.string.no_password))
            } else if (password.length < 6){
                showMessage(getString(R.string.password_must_contain_6_digits))
            } else if (login != correctLogin) {
                showMessage(getString(R.string.incorrect_login))
            } else if (password != correctPassword) {
                showMessage(getString(R.string.incorrect_password))
            } else {
                TODO()
            }
        }

        findViewById<RadioButton>(R.id.rb_theme_dark).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        findViewById<RadioButton>(R.id.rb_theme_light).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        findViewById<RadioButton>(R.id.rb_theme_system).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}