package com.dokter.ai

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun floatToInt() {
        assertEquals(98, getIntAccuracy(98.766575f))
        assertEquals(0, getIntAccuracy(0.766575f))
    }

    fun getIntAccuracy(input: Float):Int{
        val result = input.toInt()
        return result
    }
}