package com.ConnerByrne.robots

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "RobotViewModel"

class RobotViewModel : ViewModel() {
    init {
        Log.d(TAG, "instance of RobotViewModel created.")
    }

    private var turnCount = 0;

    fun advanceTurn() {
        turnCount++;
        if (turnCount > 3) {
            turnCount = 1
        }
    }

    private val robots = listOf(
        Robot(R.string.red_robot_msg, false,
            R.drawable.king_of_detroit_robot_red_large, R.drawable.king_of_detroit_robot_red_small, 0, MutableList(7) {false}),
        Robot(R.string.white_robot_msg, false,
            R.drawable.king_of_detroit_robot_white_large, R.drawable.king_of_detroit_robot_white_small, 0, MutableList(7) {false}),
        Robot(R.string.yellow_robot_msg, false,
            R.drawable.king_of_detroit_robot_yellow_large, R.drawable.king_of_detroit_robot_yellow_small, 0, MutableList(7) {false}))

    val robotsArray : List<Robot>
        get() = robots

    val currentTurn : Int
        get() = turnCount

    private var rewardsBooleans = MutableList(7) {true}

    val rewardsAvailable : List<Boolean>
        get() = rewardsBooleans

    fun removeReward(reward : Int) {
        rewardsBooleans[reward] = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "instance of RobotViewModel about to be destroyed.")
    }
}