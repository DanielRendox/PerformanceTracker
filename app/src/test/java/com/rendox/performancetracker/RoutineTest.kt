package com.rendox.performancetracker

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineTest {
    @Test
    fun convertProgressToFloat() {
        val progressFloat = Routine.convertProgressToFloat(63)
        assertThat(progressFloat).isEqualTo(0.63f)
    }

    @Test
    fun convertProgressToInt() {
        val progressInt = Routine.convertProgressToInt(0.4456f)
        assertThat(progressInt).isEqualTo(45)
    }
}