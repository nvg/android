package ca.psdev.android.dicey

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ThreadLocalRandom

class MainActivity : AppCompatActivity() {

    private var mDice1 = 1
    private var mDice2 = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (savedInstanceState != null) {
            mDice1 = savedInstanceState.getInt("dice1")
            mDice2 = savedInstanceState.getInt("dice2")
            redrawDice()
        }

        btn_roll.setOnClickListener {
            mDice1 = ThreadLocalRandom.current().nextInt(1, 7)
            mDice2 = ThreadLocalRandom.current().nextInt(1, 7)

            redrawDice()
        }
    }

    private fun redrawDice() {
        setDiceImage(iv_diceRight, mDice1)
        setDiceImage(iv_diceLeft, mDice2)
    }

    private fun setDiceImage(iv_dice: ImageView, dice: Int) {
        iv_dice.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                resources.getIdentifier(
                    "dice" + dice,
                    "drawable", packageName
                )
            )
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("dice1", mDice1)
        outState.putInt("dice2", mDice2)
        super.onSaveInstanceState(outState)
    }
}
