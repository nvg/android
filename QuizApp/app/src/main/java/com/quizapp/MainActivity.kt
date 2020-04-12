package com.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        var editorActionListener: TextView.OnEditorActionListener =
            TextView.OnEditorActionListener { v, actionId, event ->
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    btn_start.performClick()
                }
                true
            }

        et_name.setOnEditorActionListener(editorActionListener)

        btn_start.setOnClickListener {
            if (et_name.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter your name", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val intent = Intent(this@MainActivity, QuizQuestionsActivity::class.java)
            intent.putExtra(Constants.USER_NAME, et_name.text.toString())
            startActivity(intent)
            finish()
        }
    }
}