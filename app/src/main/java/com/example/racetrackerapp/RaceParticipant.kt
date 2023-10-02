package com.example.racetrackerapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

data class RaceParticipant(
    val name: String,
    val maxProgress: Int = 100,
    val progressDelayMillis: Long = 500L,
    private val progressIncrement: Int = 1,
    private val initialProgress: Int = 0,
) {
    /**
     * Satisfy parameters restrictions
     */
    init {
        require(maxProgress > 0) { "maxProgress = $maxProgress which is less than zero; must be > 0" }
        require(progressIncrement > 0) { "progressIncrement = $progressIncrement which is less than zero; must be > 0" }
        require(progressDelayMillis > 0L) { "progressDelayMillis = $progressDelayMillis which is less than zero; must be > 0" }
    }

    var currentProgress by mutableStateOf(initialProgress)
        private set

    suspend fun run() {
        try {
            while (currentProgress < maxProgress) {
                delay(progressDelayMillis)
                currentProgress += progressIncrement
//                Log.i(name, "$currentProgress")
            }
        } catch (e: CancellationException){
//            Log.e("Race participant", "$name: ${e.message}")
            throw e
        }
    }

    /**
     * Resets the [currentProgress] to 0 regardless of the value of [initialProgress]
     */
    fun reset() {
        currentProgress = 0
    }
}

/**
 * The Linear progress indicator expects progress value in the range of 0-1. This property
 * calculate the progress factor to satisfy the indicator requirements.
 * This is made as an extension function due to the special use case given by the implementation
 * detail in the progress bar
 */
val RaceParticipant.progressFactor: Float
    get() = currentProgress / maxProgress.toFloat()
