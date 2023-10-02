package com.example.racetrackerapp

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RaceParticipantTests {
    private val raceParticipant = RaceParticipant(
        name = "Test",
        maxProgress = 100,
        progressDelayMillis = 500L,
        initialProgress = 0,
        progressIncrement = 1
    )

    @Test
    fun `When race is started, the race participant's progress is updated`() = runTest {
        val expectedProgress = 1
        launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.progressDelayMillis)
        runCurrent()
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    @Test
    fun `When race is finished, the race participant's progress is updated to their maxProgress`() = runTest {
        val expectedFinalProgress: Int = 100
        launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.maxProgress * raceParticipant.progressDelayMillis)
        runCurrent()
        assertEquals(expectedFinalProgress, raceParticipant.currentProgress)
        assertEquals(expectedFinalProgress, raceParticipant.maxProgress)
    }

    @Test
    fun `When race is started and the restarted, the race participant's progress is updated to zero`() = runTest {
        val expectedProgress = 0
        launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.maxProgress / 5 * raceParticipant.progressDelayMillis)
        runCurrent()
        raceParticipant.reset()
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    @Test
    fun `When race is paused, the progress is paused too`() = runTest {
        val expectedValue = 5 // 5 seconds after initializing
        val racerJob = launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.progressDelayMillis * expectedValue)
        runCurrent()
        // Verify score is correct before cancelling the coroutine
        assertEquals(expectedValue, raceParticipant.currentProgress)
        racerJob.cancelAndJoin()
        // Verify score is correct after cancelling the coroutine
        assertEquals(expectedValue, raceParticipant.currentProgress)
        // Verify score is correct after cancelling the coroutine and advancing the time
        advanceTimeBy(raceParticipant.progressDelayMillis)
        assertEquals(expectedValue, raceParticipant.currentProgress)
    }

    @Test
    fun `When the race is paused and resumed, the progress is updated`() = runTest {
        val expectedProgress = 5 // 5 seconds after initializing
        val numberOfPauses = 2
        repeat(numberOfPauses) {
            val racerJob = launch { raceParticipant.run() }
            advanceTimeBy(raceParticipant.progressDelayMillis * expectedProgress)
            runCurrent()
            // Verify score is correct before cancelling the coroutine
            assertEquals(expectedProgress * (it + 1), raceParticipant.currentProgress)
            racerJob.cancelAndJoin()
        }
        assertEquals(numberOfPauses * expectedProgress, raceParticipant.currentProgress)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Test raceParticipant class throw exception when the maxProgress requirement is violated`() {
        RaceParticipant("Test Racer", maxProgress = 0)
        RaceParticipant("Test Racer", maxProgress = Random.nextInt(Int.MIN_VALUE, 0))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Test raceParticipant class throw exception when the progressDelayMillis requirement is violated`() {
        RaceParticipant("Test Racer", progressDelayMillis = 0L)
        RaceParticipant("Test Racer", progressDelayMillis = Random.nextLong(Long.MIN_VALUE, 0L))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Test raceParticipant class throw exception when the progressIncrement requirement is violated`() {
        RaceParticipant("Test Racer", progressIncrement = Random.nextInt(Int.MIN_VALUE, 0))
        RaceParticipant("Test Racer", progressIncrement = 0)
    }

}
