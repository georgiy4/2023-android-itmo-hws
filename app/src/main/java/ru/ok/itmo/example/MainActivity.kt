package ru.ok.itmo.example

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.dataStore

class MainActivity : AppCompatActivity(R.layout.activity) {
    val authorizationFragment = AuthorizationFragment()
    val mainFragment = MainFragment()
    val chatCreaterFragment = ChatCreaterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, authorizationFragment)
                .add(R.id.fragment_container, mainFragment)
                .add(R.id.fragment_container, chatCreaterFragment)
                .hide(chatCreaterFragment)
                .hide(mainFragment)
                .commit()
            supportFragmentManager.setFragmentResultListener("enter", this) { key, bundle ->
                mainFragment.login(bundle.get("token") as String, bundle.get("userName") as String)
                mainFragment.openChannels()
                supportFragmentManager.beginTransaction()
                    .hide(authorizationFragment)
                    .show(mainFragment)
                    .commit()
            }
            supportFragmentManager.setFragmentResultListener("back", this) { key, bundle ->
                supportFragmentManager.beginTransaction()
                    .hide(mainFragment)
                    .show(authorizationFragment)
                    .commit()
            }
            supportFragmentManager.setFragmentResultListener("start_create_chat", this) { key, bundle ->
                supportFragmentManager.beginTransaction()
                    .hide(mainFragment)
                    .show(chatCreaterFragment)
                    .addToBackStack(null)
                    .commit()
            }
            supportFragmentManager.setFragmentResultListener("create_chat", this) { key, bundle ->
                mainFragment.postMessage(bundle.get("chat_name") as String, bundle.get("message") as String)
            }
        }
    }
}