package com.ConnerByrne.robots

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel


private const val TAG = "PurchaseViewModel"
class PurchaseViewModel : ViewModel() {
    init {
        Log.d(TAG, "instance of PurchaseViewModel created.")
    }

    private var rewards = mutableListOf(
        Reward(R.string.reward_a, EXTRA_REWARD_A, 1, 1),
        Reward(R.string.reward_b, EXTRA_REWARD_B, 2, 2),
        Reward(R.string.reward_c, EXTRA_REWARD_C, 3, 3),
        Reward(R.string.reward_d, EXTRA_REWARD_D, 3, 4),
        Reward(R.string.reward_e, EXTRA_REWARD_E, 4, 5),
        Reward(R.string.reward_f, EXTRA_REWARD_F, 4, 6),
        Reward(R.string.reward_g, EXTRA_REWARD_G, 7, 7))

    private var activeRewards = mutableListOf<Reward>()

    val rewardsArray : MutableList<Reward>
        get() = rewards

    fun removeUnvailableRewards(intent : Intent) {
        val rewardsToKeep = mutableListOf<Reward>()
        for (reward in rewards) {
            if (intent.getBooleanExtra(reward.extraResource, true)) rewardsToKeep.add(reward)
        }
        rewards.clear()
        rewards.addAll(rewardsToKeep)
    }

    fun removeReward(rewardToRemove : Reward) {
        rewards.remove(rewardToRemove)
        activeRewards.remove(rewardToRemove)
    }

    val activeRewardsArray : MutableList<Reward>
        get() {
            randomizedRewards()
            return activeRewards
        }

    private fun randomizedRewards() {
        while (activeRewards.size < 3 && activeRewards.size != rewards.size) {
            var newActiveReward = rewards.random()
            while (activeRewards.contains(newActiveReward)) newActiveReward = rewards.random()
            activeRewards.add(newActiveReward)
        }
        activeRewards.sortBy { it.index }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "instance of RobotViewModel about to be destroyed.")
    }
}