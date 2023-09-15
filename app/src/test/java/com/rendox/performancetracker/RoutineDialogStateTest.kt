package com.rendox.performancetracker

import com.google.common.truth.Truth.assertThat
import com.rendox.performancetracker.feature_routine.RoutineDialogState
import org.junit.Test

class RoutineDialogStateTest {
    @Test
    fun `routineName is empty, isNameWrong = true`() {
        val state = RoutineDialogState()
        state.updateName("")
        assertThat(state.isNameWrong).isTrue()
    }

    @Test
    fun `progress is empty, isProgressWrong = true`() {
        val state = RoutineDialogState()
        state.updateProgress("")
        assertThat(state.isProgressWrong).isTrue()
    }

    @Test
    fun `progress is negative, isProgressWrong = true`() {
        val state = RoutineDialogState()
        state.updateProgress("-2")
        assertThat(state.isProgressWrong).isTrue()
    }
}
