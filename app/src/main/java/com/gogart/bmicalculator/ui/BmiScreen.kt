package com.gogart.bmicalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gogart.bmicalculator.data.AppThemeMode
import com.gogart.bmicalculator.data.BmiCalculationResult
import com.gogart.bmicalculator.data.BmiCalculator
import com.gogart.bmicalculator.data.BmiHistoryRecord
import com.gogart.bmicalculator.data.Gender
import com.gogart.bmicalculator.ui.components.BmiScaleCard
import com.gogart.bmicalculator.ui.components.FormulaCard
import com.gogart.bmicalculator.ui.components.HistoryBottomSheet
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiScreen(
    currentThemeMode: AppThemeMode,
    historyList: List<BmiHistoryRecord>,
    onThemeToggle: () -> Unit,
    onAddHistoryRecord: (BmiHistoryRecord) -> Unit,
    onDeleteHistoryRecord: (String) -> Unit,
    onClearAllHistory: () -> Unit
) {
    var gender by remember { mutableStateOf(Gender.MALE) }
    var heightCm by remember { mutableFloatStateOf(175f) }
    var weightKg by remember { mutableFloatStateOf(70f) }

    var calculationResult by remember {
        mutableStateOf<BmiCalculationResult?>(
            BmiCalculator.calculate(70f, 175f, Gender.MALE)
        )
    }

    val sheetState = rememberModalBottomSheetState()
    var showHistorySheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Калькулятор ІМТ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = when (currentThemeMode) {
                                AppThemeMode.LIGHT -> Icons.Default.LightMode
                                AppThemeMode.DARK -> Icons.Default.DarkMode
                                AppThemeMode.SYSTEM -> Icons.Default.SettingsSuggest
                            },
                            contentDescription = "Тема",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = { showHistorySheet = true }) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Історія розрахунків",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenderSelectorCard(
                selectedGender = gender,
                onGenderSelected = { gender = it }
            )

            HeightInputCard(
                heightCm = heightCm,
                onHeightChange = { heightCm = it }
            )

            WeightInputCard(
                weightKg = weightKg,
                onWeightChange = { weightKg = it }
            )

            Button(
                onClick = {
                    val res = BmiCalculator.calculate(weightKg, heightCm, gender)
                    calculationResult = res

                    val record = BmiHistoryRecord(
                        id = UUID.randomUUID().toString(),
                        timestampMillis = System.currentTimeMillis(),
                        weightKg = weightKg,
                        heightCm = heightCm,
                        bmiValue = res.bmiValue,
                        categoryName = res.category.titleUa,
                        genderName = res.gender.titleUa
                    )
                    onAddHistoryRecord(record)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Розрахувати ІМТ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            calculationResult?.let { res ->
                ResultCard(result = res)

                BmiScaleCard(
                    bmiValue = res.bmiValue,
                    category = res.category
                )

                FormulaCard(result = res)
            }
        }
    }

    if (showHistorySheet) {
        HistoryBottomSheet(
            sheetState = sheetState,
            historyList = historyList,
            onDismiss = { showHistorySheet = false },
            onDeleteRecord = onDeleteHistoryRecord,
            onClearAll = onClearAllHistory
        )
    }
}

@Composable
private fun GenderSelectorCard(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Стать",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GenderOption(
                    gender = Gender.MALE,
                    isSelected = selectedGender == Gender.MALE,
                    onClick = { onGenderSelected(Gender.MALE) },
                    modifier = Modifier.weight(1f)
                )

                GenderOption(
                    gender = Gender.FEMALE,
                    isSelected = selectedGender == Gender.FEMALE,
                    onClick = { onGenderSelected(Gender.FEMALE) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun GenderOption(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (gender == Gender.MALE) Icons.Default.Male else Icons.Default.Female,
                contentDescription = gender.titleUa,
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = gender.titleUa,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Composable
private fun HeightInputCard(
    heightCm: Float,
    onHeightChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Зріст",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${heightCm.toInt()} см",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = heightCm,
                onValueChange = onHeightChange,
                valueRange = 100f..230f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun WeightInputCard(
    weightKg: Float,
    onWeightChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Вага",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${weightKg.toInt()} кг",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = weightKg,
                onValueChange = onWeightChange,
                valueRange = 30f..220f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Composable
private fun ResultCard(
    result: BmiCalculationResult
) {
    val category = result.category

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = category.color.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ваш індекс маси тіла",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = String.format(Locale.US, "%.1f", result.bmiValue),
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = category.color
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(category.color)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = category.titleUa,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
