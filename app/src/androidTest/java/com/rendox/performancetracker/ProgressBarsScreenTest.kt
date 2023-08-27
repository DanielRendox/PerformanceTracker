package com.rendox.performancetracker

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class ProgressBarsScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val nameTextField
        get() = composeTestRule.onNodeWithTag("NameTextField")
    private val progressTextField
        get() = composeTestRule.onNodeWithTag("ProgressTextField")
    private val confirmButton
        get() = composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(R.string.confirm)) and hasClickAction()
        )
    private val nameError
        get() = composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.progress_bars_wrong_name_error_description)
        )
    private val progressError
        get() = composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.progress_bars_wrong_progress_error_description)
        )
    private val dialogTitle
        get() = composeTestRule.onNode(
            hasText(
                composeTestRule.activity.getString(R.string.progress_bars_dialog_title_add_routine)
            ) and isHeading()
        )
    private val openDialogFab
        get() = composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.progress_bars_fab_icon_description)
        )

    @Test
    fun testFab() {
        openDialogFab.performClick()
        dialogTitle.assertIsDisplayed()
    }

    @Test
    fun testErrors() {
        openDialogFab.performClick()

        confirmButton.performClick()
        nameError.assertIsDisplayed()
        progressError.assertIsDisplayed()
        confirmButton.assertIsNotEnabled()

        nameTextField.performTextInput("Routine")
        progressTextField.performTextInput("20")

        nameError.assertDoesNotExist()
        progressError.assertDoesNotExist()
        confirmButton.assertIsEnabled()

        nameTextField.performTextClearance()
        composeTestRule.onNodeWithText("Name is empty").assertIsDisplayed()
        confirmButton.assertIsNotEnabled()

        nameTextField.performTextInput("My Routine")
        nameError.assertDoesNotExist()
        confirmButton.assertIsEnabled()

        progressTextField.performTextInput("-")
        progressError.assertIsDisplayed()
        confirmButton.assertIsNotEnabled()

        progressTextField.performTextClearance()
        progressTextField.performTextInput("30")
        progressError.assertDoesNotExist()
        confirmButton.assertIsEnabled()
    }

    @Test
    fun testCancelButton() {
        openDialogFab.performClick()
        nameTextField.performTextInput("My Routine")
        progressTextField.performTextInput("30")

        composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(android.R.string.cancel)) and hasClickAction()
        ).performClick()

        dialogTitle.assertDoesNotExist()

        openDialogFab.performClick()
        nameTextField.assert(
            hasText(composeTestRule.activity.getString(R.string.progress_bars_dialog_name_text_field_label))
        )
        progressTextField.assert(
            hasText(composeTestRule.activity.getString(R.string.progress_bars_dialog_progress_text_field_label))
        )
    }

    @Test
    fun testConfirmButton() {
        openDialogFab.performClick()
        nameTextField.performTextInput("My Project")
        progressTextField.performTextInput("50")
        confirmButton.performClick()
        dialogTitle.assertDoesNotExist()
        composeTestRule.onNodeWithText("My Project").assertExists()
        composeTestRule.onRoot().printToLog("Find progress value")
        composeTestRule.onNodeWithText("50%").assertExists()
    }
}