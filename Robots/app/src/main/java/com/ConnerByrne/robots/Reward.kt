package com.ConnerByrne.robots
data class Reward (
    val rewardMessageResource : Int,
    val extraResource : String,
    val rewardCost : Int,
    var index : Int,
    var purchased : Boolean)