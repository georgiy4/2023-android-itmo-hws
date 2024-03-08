package ru.ok.itmo.example

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MainActivity : AppCompatActivity(R.layout.activity) {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TEST", "aaaaa")
        super.onCreate(savedInstanceState)

        findViewById<TextView>(R.id.text).text = Calendar.getInstance().timeInMillis.toString()

        findViewById<RadioButton>(R.id.rb_task1).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragment(FragmentProgressBar { progressBar ->
                    GlobalScope.launch(Dispatchers.IO) {
                        while (progressBar.progress < 100) {
                            delay(100)
                            withContext(Dispatchers.Main) {
                                progressBar.progress++
                            }
                        }
                    }.start()
                })
            }
        }
        findViewById<RadioButton>(R.id.rb_task2).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragment(FragmentProgressBar { progressBar ->
                    GlobalScope.launch {
                        flow {
                            while (progressBar.progress < 100) {
                                delay(100)
                                emit(Unit)
                            }
                        }
                            .flowOn(Dispatchers.IO)
                            .collect {
                                withContext(Dispatchers.Main) {
                                    progressBar.progress++
                                }
                            }
                    }
                })
            }
        }
        findViewById<RadioButton>(R.id.rb_task3).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragment(FragmentAlarmClock())
            }
        }
    }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}