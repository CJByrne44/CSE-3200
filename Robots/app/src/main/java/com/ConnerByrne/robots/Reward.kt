package com.ConnerByrne.robots
data class Reward (
    val rewardMessageResource : Int,
    val extraResource : String,
    val rewardCost : Int,
    var index : Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val reward = other as Reward
        return index == reward.index
    }
    override fun hashCode(): Int {
        return index
    }
}