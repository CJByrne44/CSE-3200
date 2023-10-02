package com.ConnerByrne.robots

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var redBotImg : ImageView
    private lateinit var whiteBotImg : ImageView
    private lateinit var yellowBotImg : ImageView
    private lateinit var messageBox : TextView
    private lateinit var reward_button : Button

    private lateinit var robotImages : MutableList<ImageView>
    private var latestPurchaseCost = 0;

    private val robots = listOf(
        Robot(R.string.red_robot_msg, false,
            R.drawable.king_of_detroit_robot_red_large, R.drawable.king_of_detroit_robot_red_small, 0),
        Robot(R.string.white_robot_msg, false,
            R.drawable.king_of_detroit_robot_white_large, R.drawable.king_of_detroit_robot_white_small, 0),
        Robot(R.string.yellow_robot_msg, false,
            R.drawable.king_of_detroit_robot_yellow_large, R.drawable.king_of_detroit_robot_yellow_small, 0))

    private val robotViewModel: RobotViewModel by viewModels()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redBotImg = findViewById(R.id.redRobot)
        whiteBotImg = findViewById(R.id.whiteRobot)
        yellowBotImg = findViewById(R.id.yellowRobot)
        messageBox = findViewById(R.id.messageBox)
        reward_button = findViewById(R.id.purchase_rewards_button)

        robotImages = mutableListOf(redBotImg, whiteBotImg, yellowBotImg)

        // Fix model if rotating screen
        setRobotsTurn()
        setRobotsImages()
        updateMessageBox()

        redBotImg.setOnClickListener { toggleImage() }
        whiteBotImg.setOnClickListener { toggleImage() }
        yellowBotImg.setOnClickListener { toggleImage() }
        reward_button.setOnClickListener { view: View ->
            val intent = RobotPurchase.newIntent(this, robots[robotViewModel.currentTurn - 1].myEnergy)
            purchaseLauncher.launch(intent)
        }

        Log.d(TAG, "Got a RobotViewModel : $robotViewModel")
    }

    private val purchaseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // capture the data gor a toast
            latestPurchaseCost = result.data?.getIntExtra(EXTRA_ROBOT_PURCHASE_MADE, 0) ?: 0

        }
    }

    private fun toggleImage() {
        robotViewModel.advanceTurn()
        setRobotsTurn()
        setRobotsImages()
        updateMessageBox()
    }

    private fun setRobotsTurn() {
        if (robotViewModel.currentTurn == 0)
            return
        for (robot in robots) { robot.myTurn = false }
        robots[robotViewModel.currentTurn - 1].myTurn = true
        robots[robotViewModel.currentTurn - 1].myEnergy += 1
    }

    private fun setRobotsImages() {
        if (robotViewModel.currentTurn == 0)
            return
        for (indy in robots.indices) {
            if (robots[indy].myTurn) {
                robotImages[indy].setImageResource(robots[indy].largeRobot)
            } else {
                robotImages[indy].setImageResource(robots[indy].smallRobot)
            }
        }
    }

    private fun updateMessageBox() {
        if (robotViewModel.currentTurn == 0)
            return
        messageBox.setText(robots[robotViewModel.currentTurn - 1].robotMessageResource)
    }
}


