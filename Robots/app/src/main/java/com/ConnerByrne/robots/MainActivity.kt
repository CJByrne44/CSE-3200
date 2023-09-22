package com.ConnerByrne.robots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var redImage : ImageView
    private lateinit var whiteImage : ImageView
    private lateinit var yellowImage : ImageView
    private lateinit var rotateClockImage : ImageView
    private lateinit var rotateCounterImage : ImageView
    var turnCount = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redImage = findViewById(R.id.redRobot)
        whiteImage = findViewById(R.id.whiteRobot)
        yellowImage = findViewById(R.id.yellowRobot)

        rotateClockImage = findViewById(R.id.rotateClockwise)
        rotateCounterImage = findViewById(R.id.rotateCounterClockwise)

        rotateClockImage.setOnClickListener { view : View -> toggleImage(true) }
        rotateCounterImage.setOnClickListener { view : View -> toggleImage(false) }
    }

    private fun toggleImage(clockwise : Boolean){
        if (turnCount == -1) {
            turnCount = 1 // Initial state should both be reds
        }
        else if (clockwise) { turnCount-- }
        else { turnCount++ }
        if (turnCount > 2) {
            turnCount = 0
        }
        if (turnCount < 0) {
            turnCount = 2
        }
        redImage.setImageResource(R.drawable.king_of_detroit_robot_red_small)
        whiteImage.setImageResource(R.drawable.king_of_detroit_robot_white_small)
        yellowImage.setImageResource(R.drawable.king_of_detroit_robot_yellow_small)
        when (turnCount) {
            1 -> { redImage.setImageResource(R.drawable.king_of_detroit_robot_red_large) }
            2 -> { whiteImage.setImageResource(R.drawable.king_of_detroit_robot_white_large) }
            0 -> { yellowImage.setImageResource(R.drawable.king_of_detroit_robot_yellow_large) }
        }
    }
}