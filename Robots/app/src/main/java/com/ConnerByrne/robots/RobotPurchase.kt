package com.ConnerByrne.robots

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ConnerByrne.robots.databinding.ActivityMainBinding
import com.ConnerByrne.robots.databinding.ActivityRobotPurchaseBinding

const val EXTRA_ROBOT_ENERGY = "com.connerbyrne.android.robots.current_robot_energy"
const val EXTRA_CURRENT_ROBOT = "com.connerbyrne.android.robots.current_robot"
const val EXTRA_REWARD_A = "com.connerbyrne.android.robots.reward_a"
const val EXTRA_REWARD_B = "com.connerbyrne.android.robots.reward_b"
const val EXTRA_REWARD_C = "com.connerbyrne.android.robots.reward_c"
const val EXTRA_REWARD_D = "com.connerbyrne.android.robots.reward_d"
const val EXTRA_REWARD_E = "com.connerbyrne.android.robots.reward_e"
const val EXTRA_REWARD_F = "com.connerbyrne.android.robots.reward_f"
const val EXTRA_REWARD_G = "com.connerbyrne.android.robots.reward_g"
private lateinit var binding : ActivityRobotPurchaseBinding
private lateinit var purchaseIntent : Intent;

private var rewards = mutableListOf(
    Reward(R.string.reward_a, EXTRA_REWARD_A, 1, 1, false),
    Reward(R.string.reward_b, EXTRA_REWARD_B, 2, 2, false),
    Reward(R.string.reward_c, EXTRA_REWARD_C, 3, 3, false),
    Reward(R.string.reward_d, EXTRA_REWARD_D, 3, 4, false),
    Reward(R.string.reward_e, EXTRA_REWARD_E, 4, 5, false),
    Reward(R.string.reward_f, EXTRA_REWARD_F, 4, 6, false),
    Reward(R.string.reward_g, EXTRA_REWARD_G, 7, 7, false))

class RobotPurchase : AppCompatActivity() {


    private lateinit var activeRewards : List<Reward>
    private var robot_energy = 0
    private var robot_index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRobotPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle information received from MainActivity
        robot_energy = intent.getIntExtra(EXTRA_ROBOT_ENERGY, 0)
        robot_index = intent.getIntExtra(EXTRA_CURRENT_ROBOT, 0)
        when (robot_index) {
            1 -> binding.purchasingRobotImage.setImageResource(R.drawable.king_of_detroit_robot_red_large)
            2 -> binding.purchasingRobotImage.setImageResource(R.drawable.king_of_detroit_robot_white_large)
            3 -> binding.purchasingRobotImage.setImageResource(R.drawable.king_of_detroit_robot_yellow_large)
        }

        val rewardsToKeep = mutableListOf<Reward>()
        for (reward in rewards) {
            if (intent.getBooleanExtra(reward.extraResource, true)) rewardsToKeep.add(reward)
        }
        rewards.clear()
        rewards.addAll(rewardsToKeep)
        Log.d("RobotPurchase", rewards.toString())

        activeRewards = randomizeRewards(rewards)
        updateRewardGraphics()


        // Send information back to MainActivity
        purchaseIntent = Intent().apply {
            putExtra(EXTRA_ROBOT_ENERGY, robot_energy)
            putExtra(EXTRA_REWARD_A, false)
            putExtra(EXTRA_REWARD_B, false)
            putExtra(EXTRA_REWARD_C, false)
            putExtra(EXTRA_REWARD_D, false)
            putExtra(EXTRA_REWARD_E, false)
            putExtra(EXTRA_REWARD_F, false)
            putExtra(EXTRA_REWARD_G, false)
        }

        binding.robotEnergyToSpend.setText(robot_energy.toString())

        binding.buyReward1.setOnClickListener{view : View ->
            makePurchase(activeRewards[0])
        }
        binding.buyReward2.setOnClickListener{view : View ->
            makePurchase(activeRewards[1])
        }
        binding.buyReward3.setOnClickListener{view : View ->
            makePurchase(activeRewards[2])
        }
    }

    companion object {
        fun newIntent(packageContext : Context,
                      robot_energy : Int,
                      robot_index : Int,
                      rewardA: Boolean,
                      rewardB: Boolean,
                      rewardC: Boolean,
                      rewardD: Boolean,
                      rewardE: Boolean,
                      rewardF: Boolean,
                      rewardG: Boolean,) : Intent {
            return Intent(packageContext, RobotPurchase::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robot_energy)
                putExtra(EXTRA_CURRENT_ROBOT, robot_index)
                putExtra(EXTRA_REWARD_A, rewardA)
                putExtra(EXTRA_REWARD_B, rewardB)
                putExtra(EXTRA_REWARD_C, rewardC)
                putExtra(EXTRA_REWARD_D, rewardD)
                putExtra(EXTRA_REWARD_E, rewardE)
                putExtra(EXTRA_REWARD_F, rewardF)
                putExtra(EXTRA_REWARD_G, rewardG)
            }
        }
    }

    private fun updateRewardGraphics() {
        binding.reward3Layout.visibility = View.INVISIBLE
        binding.reward2Layout.visibility = View.INVISIBLE
        binding.reward1Layout.visibility = View.INVISIBLE

        if (activeRewards.size > 2) {
            binding.buyReward3.text = getString(activeRewards[2].rewardMessageResource)
            binding.reward3EnergyCost.text = activeRewards[2].rewardCost.toString()
            binding.reward3Layout.visibility = View.VISIBLE
        }

        if (activeRewards.size > 1) {
            binding.buyReward2.text = getString(activeRewards[1].rewardMessageResource)
            binding.reward2EnergyCost.text = activeRewards[1].rewardCost.toString()
            binding.reward2Layout.visibility = View.VISIBLE
        }

        if (activeRewards.size > 0) {
            binding.buyReward1.text = getString(activeRewards[0].rewardMessageResource)
            binding.reward1EnergyCost.text = activeRewards[0].rewardCost.toString()
            binding.reward1Layout.visibility = View.VISIBLE
        }
    }

    fun randomizeRewards(rewards : List<Reward>): List<Reward> {
        if (rewards.size < 3) {
            return rewards.toList()
        }
        return rewards.shuffled().take(3).sortedBy { it.index }
    }

    private fun makePurchase(reward: Reward) {
        if (robot_energy >= reward.rewardCost) {
            val s2 = getString(R.string.purchased)
            val s3 = resources.getString(reward.rewardMessageResource) + " " + s2
            robot_energy -= reward.rewardCost
            binding.robotEnergyToSpend.setText(robot_energy.toString())
            Toast.makeText(this, s3, Toast.LENGTH_SHORT).show()


            rewards.remove(reward)
            purchaseIntent.putExtra(reward.extraResource, true)
            activeRewards = randomizeRewards(rewards)
            updateRewardGraphics()
            setResult(Activity.RESULT_OK, purchaseIntent)
        } else {
            Toast.makeText(this, R.string.insufficient, Toast.LENGTH_SHORT).show()
        }
    }
}