package com.rendox.performancetracker

import kotlin.math.roundToInt

data class Routine(
    var name: String,
    var progress: Float,
) {
    companion object {
        fun convertProgressToFloat(progress: Int) =
            progress / 100f

        fun convertProgressToInt(progress: Float) =
            (progress * 100).roundToInt()
    }
}