package com.ConnerByrne.robots

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private var latestPurchaseResource = 0;

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
            Log.d(TAG, robotViewModel.rewardsAvailable.toString())
            val intent =  RobotPurchase.newIntent(this,
                robotViewModel.robotsArray[robotViewModel.currentTurn - 1].myEnergy,
                robotViewModel.currentTurn,
                robotViewModel.rewardsAvailable[0],
                robotViewModel.rewardsAvailable[1],
                robotViewModel.rewardsAvailable[2],
                robotViewModel.rewardsAvailable[3],
                robotViewModel.rewardsAvailable[4],
                robotViewModel.rewardsAvailable[5],
                robotViewModel.rewardsAvailable[6])
            purchaseLauncher.launch(intent)
        }

        if (robotViewModel.currentTurn == 0)
            return

        Log.d(TAG, "Got a RobotViewModel : $robotViewModel")
    }

    private val purchaseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // capture the data for a toast
            val newRobotEnergy = result.data?.getIntExtra(EXTRA_ROBOT_ENERGY, 0) ?: 0
            val purchasedRewardA = result.data?.getBooleanExtra(EXTRA_REWARD_A, false) ?: false
            val purchasedRewardB = result.data?.getBooleanExtra(EXTRA_REWARD_B, false) ?: false
            val purchasedRewardC = result.data?.getBooleanExtra(EXTRA_REWARD_C, false) ?: false
            val purchasedRewardD = result.data?.getBooleanExtra(EXTRA_REWARD_D, false) ?: false
            val purchasedRewardE = result.data?.getBooleanExtra(EXTRA_REWARD_E, false) ?: false
            val purchasedRewardF = result.data?.getBooleanExtra(EXTRA_REWARD_F, false) ?: false
            val purchasedRewardG = result.data?.getBooleanExtra(EXTRA_REWARD_G, false) ?: false
            var currentRobot = robotViewModel.robotsArray[robotViewModel.currentTurn - 1]
            currentRobot.myEnergy = newRobotEnergy
            currentRobot.purchases[0] = purchasedRewardA || currentRobot.purchases[0]
            if (purchasedRewardA) robotViewModel.removeReward(0)
            currentRobot.purchases[1] = purchasedRewardB || currentRobot.purchases[1]
            if (purchasedRewardB) robotViewModel.removeReward(1)
            currentRobot.purchases[2] = purchasedRewardC || currentRobot.purchases[2]
            if (purchasedRewardC) robotViewModel.removeReward(2)
            currentRobot.purchases[3] = purchasedRewardD || currentRobot.purchases[3]
            if (purchasedRewardD) robotViewModel.removeReward(3)
            currentRobot.purchases[4] = purchasedRewardE || currentRobot.purchases[4]
            if (purchasedRewardE) robotViewModel.removeReward(4)
            currentRobot.purchases[5] = purchasedRewardF || currentRobot.purchases[5]
            if (purchasedRewardF) robotViewModel.removeReward(5)
            currentRobot.purchases[6] = purchasedRewardG || currentRobot.purchases[6]
            if (purchasedRewardG) robotViewModel.removeReward(6)
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
        for (robot in robotViewModel.robotsArray) { robot.myTurn = false }
        robotViewModel.robotsArray[robotViewModel.currentTurn - 1].myTurn = true
        robotViewModel.robotsArray[robotViewModel.currentTurn - 1].myEnergy += 1
        var toastText = ""
        for ((index, rewardBoolean) in robotViewModel.robotsArray[robotViewModel.currentTurn - 1].purchases.withIndex()) {
            if (!rewardBoolean) continue
            if  (toastText.length != 0) toastText += ", "
            when (index) {
                0 -> toastText += "A"
                1 -> toastText += "B"
                2 -> toastText += "C"
                3 -> toastText += "D"
                4 -> toastText += "E"
                5 -> toastText += "F"
                6 -> toastText += "G"
            }
        }
        if  (toastText.length != 0) Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun setRobotsImages() {
        if (robotViewModel.currentTurn == 0)
            return
        for (indy in robotViewModel.robotsArray.indices) {
            if (robotViewModel.robotsArray[indy].myTurn) {
                robotImages[indy].setImageResource(robotViewModel.robotsArray[indy].largeRobot)
            } else {
                robotImages[indy].setImageResource(robotViewModel.robotsArray[indy].smallRobot)
            }
        }
    }

    private fun updateMessageBox() {
        if (robotViewModel.currentTurn == 0)
            return
        messageBox.setText(robotViewModel.robotsArray[robotViewModel.currentTurn - 1].robotMessageResource)
    }
}


