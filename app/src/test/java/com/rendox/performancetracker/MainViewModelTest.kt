package com.rendox.performancetracker

import org.junit.Before
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MainViewModelTest {

    private val viewModel = MainViewModel()

    @Before
    fun setUp() {
        viewModel.addRoutine(Routine("Dummy 1", 0.01f))
        viewModel.addRoutine(Routine("Dummy 2", 0.02f))
        viewModel.addRoutine(Routine("Dummy 3", 0.03f))
    }

    @Test
    fun `Edit a routine by index, works properly`() {
        val controlElement = Routine("New routine", 1f)
        viewModel.editRoutine(1, controlElement)
        assertThat(viewModel.routineList.value[1]).isEqualTo(controlElement)
    }
}