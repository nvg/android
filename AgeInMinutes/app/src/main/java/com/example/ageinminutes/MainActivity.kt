package com.example.ageinminutes

import android.app.DatePickerDialog
import android.icu.util.DateInterval
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSelectDate.setOnClickListener { view ->
            clickDatePicker(view)
        }
    }

    fun clickDatePicker(view: View) {
        val cal = Calendar.getInstance();
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                doLogic(
                    year,
                    month,
                    day
                )
            },
            year,
            month,
            day
        )
        dialog.datePicker.setMaxDate(Date().time)
        dialog.show()
    }

    fun doLogic(year: Int, month: Int, day: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)

        val delta = Calendar.getInstance().timeInMillis - cal.timeInMillis
        val dayCount = TimeUnit.DAYS.convert(delta, TimeUnit.MILLISECONDS)
        tvAgeInDays.text = "$dayCount"

        val df: SimpleDateFormat = SimpleDateFormat("MMMM d, yyyy")
        tvSelectedDate.text = df.format(cal.time)
    }
}
