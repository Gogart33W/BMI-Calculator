package com.gogart.bmicalculator.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

enum class AppThemeMode {
    SYSTEM, LIGHT, DARK
}

class PreferencesRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("bmi_calculator_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_HISTORY = "key_history_json"
    }

    fun getThemeMode(): AppThemeMode {
        val name = prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.name)
        return try {
            AppThemeMode.valueOf(name ?: AppThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            AppThemeMode.SYSTEM
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }

    fun getHistory(): List<BmiHistoryRecord> {
        val json = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<BmiHistoryRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addHistoryRecord(record: BmiHistoryRecord) {
        val current = getHistory().toMutableList()
        current.add(0, record)
        if (current.size > 50) {
            current.removeAt(current.lastIndex)
        }
        saveHistory(current)
    }

    fun clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply()
    }

    fun deleteHistoryRecord(id: String) {
        val current = getHistory().filterNot { it.id == id }
        saveHistory(current)
    }

    private fun saveHistory(list: List<BmiHistoryRecord>) {
        val json = gson.toJson(list)
        prefs.edit().putString(KEY_HISTORY, json).apply()
    }
}
