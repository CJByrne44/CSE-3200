package com.ConnerByrne.robots

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_ROBOT_ENERGY = "com.connerbyrne.android.robots.current_robot_energy"
const val EXTRA_ROBOT_PURCHASE_MADE = "com.connerbyrne.android.robots.robot_purchase"

class RobotPurchase : AppCompatActivity() {

    private lateinit var reward_button_1 : Button
    private lateinit var reward_button_2 : Button
    private lateinit var reward_button_3 : Button
    private lateinit var reward_cost_1 : TextView
    private lateinit var reward_cost_2 : TextView
    private lateinit var reward_cost_3 : TextView
    private lateinit var robot_energy_available : TextView
    private lateinit var activeRewards : List<Reward>
    private var robot_energy = 0

    private val robotViewModel: RobotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robot_purchase)

        reward_button_1 = findViewById(R.id.buy_reward_1)
        reward_cost_1 = findViewById(R.id.reward_1_energy_cost)
        reward_button_2 = findViewById(R.id.buy_reward_2)
        reward_cost_2 = findViewById(R.id.reward_2_energy_cost)
        reward_button_3 = findViewById(R.id.buy_reward_3)
        reward_cost_3 = findViewById(R.id.reward_3_energy_cost)

        activeRewards = robotViewModel.randomizedRewards()
        updateRewardGraphics()

        robot_energy_available = findViewById(R.id.robot_energy_to_spend)

        robot_energy = intent.getIntExtra(EXTRA_ROBOT_ENERGY, 0)
        robot_energy_available.setText(robot_energy.toString())

        reward_button_1.setOnClickListener{view : View ->
            makePurchase(activeRewards[0])
        }
        reward_button_2.setOnClickListener{view : View ->
            makePurchase(activeRewards[1])
        }
        reward_button_3.setOnClickListener{view : View ->
            makePurchase(activeRewards[2])
        }
    }

    companion object {
        fun newIntent(packageContext : Context, robot_energy : Int, rewardMessageResource: Int) : Intent {
            return Intent(packageContext, RobotPurchase::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robot_energy)
                putExtra(EXTRA_ROBOT_PURCHASE_MADE, rewardMessageResource)
            }
        }
    }

    private fun updateRewardGraphics() {
        val reward_3_layout = findViewById<LinearLayout>(R.id.reward_3_layout)
        reward_3_layout.visibility = View.INVISIBLE
        val reward_2_layout = findViewById<LinearLayout>(R.id.reward_2_layout)
        reward_2_layout.visibility = View.INVISIBLE
        val reward_1_layout = findViewById<LinearLayout>(R.id.reward_1_layout)
        reward_1_layout.visibility = View.INVISIBLE

        if (activeRewards.size > 2) {
            reward_button_3.text = getString(activeRewards[2].rewardMessageResource)
            reward_cost_3.text = activeRewards[2].rewardCost.toString()
            reward_3_layout.visibility = View.VISIBLE
        }

        if (activeRewards.size > 1) {
            reward_button_2.text = getString(activeRewards[1].rewardMessageResource)
            reward_cost_2.text = activeRewards[1].rewardCost.toString()
            reward_2_layout.visibility = View.VISIBLE
        }

        if (activeRewards.size > 0) {
            reward_button_1.text = getString(activeRewards[0].rewardMessageResource)
            reward_cost_1.text = activeRewards[0].rewardCost.toString()
            reward_1_layout.visibility = View.VISIBLE
        }
    }

    private fun makePurchase(reward: Reward) {
        if (robot_energy >= reward.rewardCost) {
            val s2 = getString(R.string.purchased)
            val s3 = resources.getString(reward.rewardMessageResource) + " " + s2
            robot_energy -= reward.rewardCost
            robot_energy_available.setText(robot_energy.toString())
            Toast.makeText(this, s3, Toast.LENGTH_SHORT).show()


            robotViewModel.removeReward(reward)
            val data = Intent().apply{
                putExtra(EXTRA_ROBOT_ENERGY, robot_energy)
                putExtra(EXTRA_ROBOT_PURCHASE_MADE, reward.rewardMessageResource)
            }
            setResult(Activity.RESULT_OK, data)
            activeRewards = robotViewModel.randomizedRewards()
            updateRewardGraphics()
        } else {
            Toast.makeText(this, R.string.insufficient, Toast.LENGTH_SHORT).show()
        }
    }
}