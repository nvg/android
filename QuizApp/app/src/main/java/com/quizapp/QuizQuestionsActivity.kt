package com.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import java.lang.reflect.Type

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers = 0
    private var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        mUserName = intent.getStringExtra(Constants.USER_NAME)
        mQuestionList = Constants.getQuestions()

        setQuestion()

        for (o in getOptions()) {
            o.setOnClickListener(this)
        }

        btn_submit.setOnClickListener {
            var optionSelected = mSelectedOptionPosition == 0
            if (optionSelected) {
                mCurrentPosition++

                var hasMoreQuestions = mCurrentPosition <= mQuestionList!!.size
                if (hasMoreQuestions) {
                    setQuestion()
                    return@setOnClickListener
                }

                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(Constants.USER_NAME, mUserName)
                intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList!!.size)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            val question = mQuestionList?.get(mCurrentPosition - 1)
            val isCorrectAnswer = question!!.correctAnswer == mSelectedOptionPosition
            if (!isCorrectAnswer) {
                answerView(mSelectedOptionPosition, R.drawable.incorrect_option_border_bg)
            } else {
                mCorrectAnswers++
            }
            answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

            val isEndReached = mCurrentPosition == mQuestionList!!.size
            if (isEndReached) {
                btn_submit.text = "FINISH"
            } else {
                btn_submit.text = "GO TO NEXT"
            }

            mSelectedOptionPosition = 0
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        getOptions().get(answer - 1).background = ContextCompat.getDrawable(this, drawableView)
    }

    private fun setQuestion() {
        val question = mQuestionList!![mCurrentPosition - 1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionList!!.size) {
            btn_submit.text = "FINISH"
        } else {
            btn_submit.text = "SUBMIT"
        }

        pb_progress.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition / ${pb_progress.max}"

        iv_image.setImageResource(question!!.image)

        tv_question.text = question.question
        tv_option1.text = question.optionOne
        tv_option2.text = question.optionTwo
        tv_option3.text = question.optionThree
        tv_option4.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = getOptions()

        for (o in options) {
            o.setTextColor(Color.parseColor("#7A8089"))
            o.typeface = Typeface.DEFAULT
            o.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun getOptions(): ArrayList<TextView> {
        val options = arrayListOf<TextView>(
            tv_option1,
            tv_option2,
            tv_option3,
            tv_option4
        )
        return options
    }

    override fun onClick(v: View?) {
        val tv: TextView = findViewById<TextView>(v!!.id)
        selectedOptionView(tv, getOptions().indexOf(tv) + 1)
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.typeface = Typeface.DEFAULT
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
        tv.setTypeface(tv.typeface, Typeface.BOLD)
    }
}