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

    val currentTurn : Int
        get() = turnCount

    private var rewards = mutableListOf(
        Reward(R.string.reward_a, 1, 1),
        Reward(R.string.reward_b, 2, 2),
        Reward(R.string.reward_c, 3, 3),
        Reward(R.string.reward_d, 3, 4),
        Reward(R.string.reward_e, 4, 5),
        Reward(R.string.reward_f, 4, 6),
        Reward(R.string.reward_g, 7, 7))

    val rewardsList : List<Reward>
        get() = rewards

    val numberOfRewards : Int
        get() = rewards.size

    fun randomizedRewards(): List<Reward> {
        if (rewards.size < 3) {
            return rewards.toList()
        }
        return rewards.shuffled().take(3).sortedBy { it.index }
    }

    fun removeReward(reward : Reward) {
        rewards.remove(reward)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "instance of RobotViewModel about to be destroyed.")
    }
}