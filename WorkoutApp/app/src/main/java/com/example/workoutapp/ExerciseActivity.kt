package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_info.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseStatusAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(tb_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tb_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        exerciseList = Constants.defaultExerciseList()
        setupRestView()

        tts = TextToSpeech(this, this)
        setupExerciseStatusRecyclerView()
    }

    private fun setupExerciseStatusRecyclerView() {
        rv_exercise_status.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rv_exercise_status.adapter = exerciseStatusAdapter
    }

    public override fun onDestroy() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setupRestView() {
        ll_rest_view.visibility = View.VISIBLE
        ll_exercise_view.visibility = View.GONE

        tv_upcoming_exercise_name.text =
            if (currentExercisePosition < exerciseList!!.size)
                exerciseList!![currentExercisePosition + 1].name
            else ""

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Init error", e)
        }

        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        progressBar.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10 - restProgress
                tvTimer.text =
                    (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].isSelected = true
                exerciseStatusAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setupExerciseView() {
        ll_rest_view.visibility = View.GONE
        ll_exercise_view.visibility = View.VISIBLE

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        iv_exercise.setImageResource(exerciseList!![currentExercisePosition].image)
        var exerciseName = exerciseList!![currentExercisePosition].name
        tv_exercise_name.text = exerciseName

        speakOut(exerciseName)
        setExerciseProgressBar()

    }

    private fun setExerciseProgressBar() {
        pb_exercise.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000) {
            var isPaused = false

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                pb_exercise.progress = 30 - exerciseProgress
                tvExerciseTimer.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].isSelected = false
                    exerciseList!![currentExercisePosition].isCompleted = true
                    exerciseStatusAdapter!!.notifyDataSetChanged()

                    setupRestView()
                } else {
                    finish()
                    var intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language for " + Locale.US + " is not supported or not installed")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun customDialogForBackButton() {
        var customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_info)

        customDialog.btn_no.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.btn_yes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.show()
    }
}