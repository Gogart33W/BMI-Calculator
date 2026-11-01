package com.gogart.bmicalculator

import com.gogart.bmicalculator.data.BmiCalculator
import com.gogart.bmicalculator.data.BmiCategory
import com.gogart.bmicalculator.data.Gender
import org.junit.Assert.assertEquals
import org.junit.Test

class BmiCalculatorTest {

    @Test
    fun testNormalBmiCalculation() {
        val result = BmiCalculator.calculate(weightKg = 70f, heightCm = 175f, gender = Gender.MALE)
        assertEquals(22.9f, result.bmiValue, 0.1f)
        assertEquals(BmiCategory.NORMAL, result.category)
    }

    @Test
    fun testUnderweightBmiCalculation() {
        val result = BmiCalculator.calculate(weightKg = 45f, heightCm = 175f, gender = Gender.FEMALE)
        assertEquals(14.7f, result.bmiValue, 0.1f)
        assertEquals(BmiCategory.UNDERWEIGHT, result.category)
    }

    @Test
    fun testOverweightBmiCalculation() {
        val result = BmiCalculator.calculate(weightKg = 85f, heightCm = 175f, gender = Gender.MALE)
        assertEquals(27.8f, result.bmiValue, 0.1f)
        assertEquals(BmiCategory.OVERWEIGHT, result.category)
    }

    @Test
    fun testObeseBmiCalculation() {
        val result = BmiCalculator.calculate(weightKg = 110f, heightCm = 175f, gender = Gender.MALE)
        assertEquals(35.9f, result.bmiValue, 0.1f)
        assertEquals(BmiCategory.OBESE_CLASS_2, result.category)
    }
}
