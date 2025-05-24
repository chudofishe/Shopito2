package com.chudofishe.shopito.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import kotlinx.coroutines.delay

@Composable
fun HomeBottomSheet(
    textInputValue: String,
    onTextInputValueChanged: (String) -> Unit,
    onAddButtonClicked: () -> Unit,
    onCategoryButtonClicked: () -> Unit,
    selectedCategory: Category = Category.OTHER,
    shouldRequestFocus: Boolean = false,
    onFocusRequested: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(shouldRequestFocus) {
        if (shouldRequestFocus) {
            delay(100)
            focusRequester.requestFocus()
            keyboardController?.show()
            onFocusRequested()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onCategoryButtonClicked
            ) {
                Image(
                    painter = painterResource(id = selectedCategory.drawable),
                    contentDescription = "Category image"
                )
            }
            TextField(
                value = textInputValue,
                onValueChange = onTextInputValueChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = { Text("Add item") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (textInputValue.isNotBlank()) {
                            onAddButtonClicked()
                        }
                    }
                ),
                singleLine = true
            )
            if (textInputValue.isNotBlank()) {
                IconButton(
                    onClick = {
                        onAddButtonClicked()
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
        // Для превью показываем оба состояния
        // В реальном компоненте используйте AnimatedVisibility с проверкой состояния
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Description") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeBottomSheetPreview(

) {
    ShopitoTheme {
        HomeBottomSheet(
            onAddButtonClicked = {},
            onCategoryButtonClicked = {},
            textInputValue = "hello",
            onTextInputValueChanged = {},
            selectedCategory = Category.OTHER
        )
    }
}