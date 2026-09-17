package com.withus.choose

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.withus.choose.ui.SomaticScreen
import com.withus.choose.ui.SomaticViewModel
import com.withus.choose.ui.components.HistorySheet
import com.withus.choose.ui.screens.CalibrationScreen
import com.withus.choose.ui.screens.DilemmaScreen
import com.withus.choose.ui.screens.FlashTestScreen
import com.withus.choose.ui.screens.VerdictScreen
import com.withus.choose.ui.theme.ObsidianBlack
import com.withus.choose.ui.theme.SomaticTheme

@Composable
fun SomaticApp(viewModel: SomaticViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val historyList by viewModel.historyList.collectAsState()
    var showHistory by remember { mutableStateOf(false) }

    // Intercept back button when inside calibration, flash test, or verdict
    BackHandler(enabled = currentScreen != SomaticScreen.DILEMMA) {
        viewModel.resetToDilemma()
    }

    SomaticTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .statusBarsPadding()
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    SomaticScreen.DILEMMA -> DilemmaScreen(
                        viewModel = viewModel,
                        onOpenHistory = { showHistory = true }
                    )
                    SomaticScreen.CALIBRATION -> CalibrationScreen(viewModel = viewModel)
                    SomaticScreen.FLASH_TEST -> FlashTestScreen(viewModel = viewModel)
                    SomaticScreen.VERDICT -> VerdictScreen(viewModel = viewModel)
                }
            }

            if (showHistory) {
                HistorySheet(
                    history = historyList,
                    onDismiss = { showHistory = false },
                    onDeleteItem = { entity -> viewModel.deleteHistoryItem(entity) },
                    onClearAll = { viewModel.clearAllHistory() }
                )
            }
        }
    }
}
