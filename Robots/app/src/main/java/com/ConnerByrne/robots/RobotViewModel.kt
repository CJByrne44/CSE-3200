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

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "instance of RobotViewModel about to be destroyed.")
    }
}