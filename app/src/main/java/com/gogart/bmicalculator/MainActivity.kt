package com.gogart.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gogart.bmicalculator.data.AppThemeMode
import com.gogart.bmicalculator.data.PreferencesRepository
import com.gogart.bmicalculator.ui.BmiScreen
import com.gogart.bmicalculator.ui.theme.BmiCalculatorTheme

class MainActivity : ComponentActivity() {

    private lateinit var prefsRepo: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefsRepo = PreferencesRepository(applicationContext)

        setContent {
            var themeMode by remember { mutableStateOf(prefsRepo.getThemeMode()) }
            var historyList by remember { mutableStateOf(prefsRepo.getHistory()) }

            BmiCalculatorTheme(themeMode = themeMode) {
                BmiScreen(
                    currentThemeMode = themeMode,
                    historyList = historyList,
                    onThemeToggle = {
                        val nextMode = when (themeMode) {
                            AppThemeMode.LIGHT -> AppThemeMode.DARK
                            AppThemeMode.DARK -> AppThemeMode.SYSTEM
                            AppThemeMode.SYSTEM -> AppThemeMode.LIGHT
                        }
                        themeMode = nextMode
                        prefsRepo.setThemeMode(nextMode)
                    },
                    onAddHistoryRecord = { record ->
                        prefsRepo.addHistoryRecord(record)
                        historyList = prefsRepo.getHistory()
                    },
                    onDeleteHistoryRecord = { id ->
                        prefsRepo.deleteHistoryRecord(id)
                        historyList = prefsRepo.getHistory()
                    },
                    onClearAllHistory = {
                        prefsRepo.clearHistory()
                        historyList = prefsRepo.getHistory()
                    }
                )
            }
        }
    }
}
