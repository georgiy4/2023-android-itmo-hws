package ru.ok.itmo.example

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Calendar
import java.util.Date

class FragmentAlarmClock: Fragment(R.layout.frame_alarm_clock) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener{
            val calendar = Calendar.getInstance()

            TimePickerDialog(
                requireActivity(),
                { _, hours, minutes->
                    onTimePicked(hours, minutes)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun onTimePicked(hours: Int, minutes: Int) {
        val now = Calendar.getInstance()
        val alarmTime = Calendar.getInstance()
        alarmTime[Calendar.HOUR_OF_DAY] = hours
        alarmTime[Calendar.MINUTE] = minutes
        if (alarmTime.before(now)) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1)
        }
        AlarmWorker.scheduleAlarmWork(
            requireContext(),
            alarmTime.timeInMillis - now.timeInMillis,
            getString(R.string.alarm),
            getString(R.string.time_to_get_up)
        )
    }
}