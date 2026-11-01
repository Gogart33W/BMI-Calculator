package com.gogart.bmicalculator.data

import java.util.Locale

object BmiCalculator {

    fun calculate(weightKg: Float, heightCm: Float, gender: Gender): BmiCalculationResult {
        val heightMeters = heightCm / 100f
        val heightSquared = heightMeters * heightMeters
        val rawBmi = if (heightSquared > 0f) weightKg / heightSquared else 0f
        val roundedBmi = (Math.round(rawBmi * 10f) / 10f).toFloat()

        val category = determineCategory(roundedBmi)

        val step1 = String.format(Locale.US, "%.0f см ÷ 100 = %.2f м", heightCm, heightMeters)
        val step2 = String.format(Locale.US, "%.2f м × %.2f м = %.4f м²", heightMeters, heightMeters, heightSquared)
        val step3 = String.format(Locale.US, "%.1f кг ÷ %.4f м² = %.1f кг/м²", weightKg, heightSquared, roundedBmi)

        return BmiCalculationResult(
            bmiValue = roundedBmi,
            category = category,
            heightCm = heightCm,
            weightKg = weightKg,
            gender = gender,
            step1HeightMeters = step1,
            step2HeightSquared = step2,
            step3FinalCalculation = step3
        )
    }

    fun determineCategory(bmi: Float): BmiCategory {
        return when {
            bmi < 18.5f -> BmiCategory.UNDERWEIGHT
            bmi < 25.0f -> BmiCategory.NORMAL
            bmi < 30.0f -> BmiCategory.OVERWEIGHT
            bmi < 35.0f -> BmiCategory.OBESE_CLASS_1
            bmi < 40.0f -> BmiCategory.OBESE_CLASS_2
            else -> BmiCategory.OBESE_CLASS_3
        }
    }
}
