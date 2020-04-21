package ca.psdev.android.dicey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        btn_roll.setOnClickListener {
            val dice1 = ThreadLocalRandom.current().nextInt(1, 6)
            val dice2 = ThreadLocalRandom.current().nextInt(1, 6)

            iv_diceLeft.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    getResources().getIdentifier("dice" + dice1, "drawable", getPackageName())
                )
            )

            iv_diceRight.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    getResources().getIdentifier("dice" + dice2, "drawable", getPackageName())
                )
            )
        }
    }
}
