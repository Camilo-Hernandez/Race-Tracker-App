package com.example.racetrackerapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.racetrackerapp.ui.theme.RaceTrackerAppTheme
import com.example.racetrackerapp.ui.theme.md_theme_light_inversePrimary
import com.example.racetrackerapp.ui.theme.md_theme_light_primary
import com.example.racetrackerapp.ui.theme.md_theme_light_surfaceTint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun RaceTrackerApp() {

    val playerOne: RaceParticipant = remember { RaceParticipant("Player 1", progressIncrement = 1) }
    val playerTwo: RaceParticipant = remember { RaceParticipant("Player 2", progressIncrement = 2) }
    var raceInProgress by rememberSaveable { mutableStateOf(false) }

    if (raceInProgress) {
        LaunchedEffect(key1 = playerOne, key2 = playerTwo) {
            coroutineScope {
                launch { playerOne.run() }
                launch { playerTwo.run() }
            }
            raceInProgress = false
        }
    }

    

    RaceTrackerScreen(
        playerOne = playerOne,
        playerTwo = playerTwo,
        isRunning = raceInProgress,
        onRunStateChange = {
            raceInProgress = it
        },
    )
}

@Composable
fun RaceTrackerScreen(
    modifier: Modifier = Modifier,
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant,
    isRunning: Boolean,
    onRunStateChange: (Boolean) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ScreenTitle()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            PersonImage(modifier)
            StatusIndicator(
                participantName = playerOne.name,
                currentProgress = playerOne.currentProgress,
                maxProgress = stringResource(R.string.progress_percentage, playerOne.maxProgress),
                progressFactor = playerOne.progressFactor,
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.padding_large)))
            StatusIndicator(
                participantName = playerTwo.name,
                currentProgress = playerTwo.currentProgress,
                maxProgress = stringResource(R.string.progress_percentage, playerOne.maxProgress),
                progressFactor = playerTwo.progressFactor,
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.padding_large)))
            RaceControls(isRunning = isRunning, onRunStateChange = onRunStateChange, onReset = {
                playerOne.reset()
                playerTwo.reset()
                onRunStateChange(false)
            })
        }
    }
}

@Composable
private fun PersonImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_walk_player),
        contentDescription = stringResource(id = R.string.app_name),
        modifier = modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
    )
}

@Composable
private fun ScreenTitle() {
    Text(
        text = stringResource(id = R.string.run_a_race),
        style = MaterialTheme.typography.headlineLarge,
        fontFamily = FontFamily.Cursive,
    )
}


@Composable
private fun StatusIndicator(
    participantName: String,
    progressFactor: Float,
    currentProgress: Int,
    maxProgress: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Text(
            text = participantName,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small))
        )
        Column(modifier = modifier) {
            LinearProgressIndicator(
                color = md_theme_light_primary,
                trackColor = md_theme_light_inversePrimary,
                progress = progressFactor,
                modifier = modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.progress_indicator_height))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.progress_indicator_corner_radius)))
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.progress_percentage, currentProgress),
                    textAlign = TextAlign.Start,
                    modifier = modifier.weight(1f)
                )
                Text(
                    text = maxProgress, textAlign = TextAlign.End, modifier = modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RaceControls(
    isRunning: Boolean,
    onRunStateChange: (Boolean) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { onRunStateChange(!isRunning) },
            colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primary)
        ) {
            Text(
                text = if (isRunning) stringResource(R.string.pause)
                else stringResource(id = R.string.start)
            )
        }
        ElevatedButton(
            onClick = onReset, colors = ButtonDefaults.elevatedButtonColors(
                containerColor = md_theme_light_surfaceTint, contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.reset))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_XL)
@Preview(showBackground = true, showSystemUi = true, device = Devices.TABLET)
@Composable
fun RaceTrackerAppPreview() {
    RaceTrackerAppTheme {
        RaceTrackerApp()
    }
}
