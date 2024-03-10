package ru.ok.itmo.example

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity) {
    var navigationFragment: NavigationFragment? = null
    var mainFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            mainFragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, mainFragment)
                .commit()
            supportFragmentManager.setFragmentResultListener("start", this) { key, bundle ->
                navigationFragment = NavigationFragment()
                supportFragmentManager.beginTransaction()
                    .hide(mainFragment)
                    .add(R.id.fragment_container, navigationFragment!!)
                    .commit()
            }
            supportFragmentManager.setFragmentResultListener("goMain", this) { key, bundle ->
                supportFragmentManager.beginTransaction()
                    .show(mainFragment)
                    .remove(navigationFragment!!)
                    .commit()
                navigationFragment = null
            }
        }
    }

    override fun onBackPressed() {
        if (navigationFragment == null) {
            super.onBackPressed()
        } else {
            if (navigationFragment!!.onBackPressed()) {
                supportFragmentManager.beginTransaction()
                    .show(mainFragment!!)
                    .remove(navigationFragment!!)
                    .commit()
                navigationFragment = null
            }
        }
    }
}