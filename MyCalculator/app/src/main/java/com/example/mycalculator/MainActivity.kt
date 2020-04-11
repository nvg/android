package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    private var operand = BigDecimal.ZERO
    private var operator = ""
    private var isStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput.text = "0"
    }

    fun onDigit(view: View) {
        if (isStart || tvInput.text == "0") {
            tvInput.text = ""
            isStart = false
        }
        tvInput.append((view as Button).text)
    }

    fun onOp(view: View) {
        isStart = true

        operator = (view as Button).text.toString()
        operand = textToNumber()
    }

    fun onEqual(view: View) {
        if (operator == "") {
            return
        }

        isStart = true

        var result: BigDecimal = BigDecimal.ZERO
        try {
            when (operator) {
                "+" -> result = operand.add(textToNumber())
                "-" -> result = operand.subtract(textToNumber())
                "/" -> result = operand.divide(textToNumber())
                "*" -> result = operand.multiply(textToNumber())
            }
        } catch (e: Throwable) {
            tvInput.text = e.message
        }

        operand = BigDecimal.ZERO
        operator = ""

        tvInput.text = result.toString()
    }

    fun textToNumber(): BigDecimal {
        if (tvInput.text == "") {
            return BigDecimal.ZERO
        }

        try {
            return BigDecimal(tvInput.text.toString())
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG)
        }
        return BigDecimal.ZERO
    }

    fun onDecimal(view: View) {
        if (isStart) {
            tvInput.text = ""
            isStart = false
        }

        if (tvInput.text.contains(".")) {
            return
        }
        onDigit(view)
    }

    fun onClear(view: View) {
        tvInput.text = "0"
        operator = ""
        operand = BigDecimal.ZERO
        isStart = true
    }
}
