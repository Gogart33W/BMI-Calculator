package com.gogart.bmicalculator.data

import androidx.compose.ui.graphics.Color

enum class Gender(val titleUa: String) {
    MALE("Чоловік"),
    FEMALE("Жінка")
}

enum class BmiCategory(
    val titleUa: String,
    val rangeText: String,
    val minBmi: Float,
    val maxBmi: Float,
    val colorHex: Long,
    val recommendation: String
) {
    UNDERWEIGHT(
        titleUa = "Дефіцит маси тіла",
        rangeText = "< 18.5",
        minBmi = 0f,
        maxBmi = 18.5f,
        colorHex = 0xFF2196F3,
        recommendation = "Рекомендується збалансувати харчування та проконсультуватись із лікарем для нормалізації маси тіла."
    ),
    NORMAL(
        titleUa = "Нормальна маса тіла",
        rangeText = "18.5 – 24.9",
        minBmi = 18.5f,
        maxBmi = 25.0f,
        colorHex = 0xFF4CAF50,
        recommendation = "Чудовий результат! Підтримуйте поточний активний спосіб життя та здорове харчування."
    ),
    OVERWEIGHT(
        titleUa = "Надлишкова маса тіла",
        rangeText = "25.0 – 29.9",
        minBmi = 25.0f,
        maxBmi = 30.0f,
        colorHex = 0xFFFF9800,
        recommendation = "Помірне перевищення норми. Рекомендується додати помірні фізичні навантаження та зменшити калорійність."
    ),
    OBESE_CLASS_1(
        titleUa = "Ожиріння I ступеня",
        rangeText = "30.0 – 34.9",
        minBmi = 30.0f,
        maxBmi = 35.0f,
        colorHex = 0xFFFF5722,
        recommendation = "Підвищений ризик для здоров'я. Рекомендується звернутися до фахівця для розробки плану схуднення."
    ),
    OBESE_CLASS_2(
        titleUa = "Ожиріння II ступеня",
        rangeText = "35.0 – 39.9",
        minBmi = 35.0f,
        maxBmi = 40.0f,
        colorHex = 0xFFF44336,
        recommendation = "Високий ризик розвитку супутніх захворювань. Необхідна консультація лікаря-дієтолога."
    ),
    OBESE_CLASS_3(
        titleUa = "Ожиріння III ступеня (Морбідне)",
        rangeText = "≥ 40.0",
        minBmi = 40.0f,
        maxBmi = 100.0f,
        colorHex = 0xFFB71C1C,
        recommendation = "Критичний рівень. Потребує термінового медичного спостереження та комплексного лікування."
    );

    val color: Color
        get() = Color(colorHex)
}

data class BmiCalculationResult(
    val bmiValue: Float,
    val category: BmiCategory,
    val heightCm: Float,
    val weightKg: Float,
    val gender: Gender,
    val step1HeightMeters: String,
    val step2HeightSquared: String,
    val step3FinalCalculation: String
)

data class BmiHistoryRecord(
    val id: String,
    val timestampMillis: Long,
    val weightKg: Float,
    val heightCm: Float,
    val bmiValue: Float,
    val categoryName: String,
    val genderName: String
)
