package com.chudofishe.shopito.ui.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun StatefulOutlinedTextField(
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    var text by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text, // Pass value argument
        onValueChange = {
            onValueChange(it)
            text = it
        }, // Pass onValueChange lambda
        modifier = modifier, // Pass modifier
        enabled = enabled, // Pass enabled state
        readOnly = readOnly, // Pass readOnly state
        textStyle = textStyle, // Pass text style
        label = label, // Pass label composable
        placeholder = placeholder, // Pass placeholder composable
        leadingIcon = leadingIcon, // Pass leading icon composable
        trailingIcon = trailingIcon, // Pass trailing icon composable
        prefix = prefix, // Pass prefix composable
        suffix = suffix, // Pass suffix composable
        supportingText = supportingText, // Pass supporting text composable
        isError = isError, // Pass error state
        visualTransformation = visualTransformation, // Pass visual transformation
        keyboardOptions = keyboardOptions, // Pass keyboard options
        keyboardActions = keyboardActions, // Pass keyboard actions
        singleLine = singleLine, // Pass singleLine
        maxLines = maxLines, // Pass maxLines
        minLines = minLines, // Pass minLines
        interactionSource = interactionSource, // Pass interaction source
        shape = shape, // Pass shape
        colors = colors // Pass colors
    )
}
