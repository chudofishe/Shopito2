package com.chudofishe.shopito.ui.add_list_item.create_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.ObserveAsEvents
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.navigation.NavigateBack
import com.chudofishe.shopito.ui.composables.StatefulOutlinedTextField
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateItemScreen(
    setOnCheckClicked: (() -> Unit) -> Unit,
    onNavigateUp: () -> Unit
) {

    val viewModel: CreateItemScreenViewModel = koinViewModel()
    val createScreenState by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.navigationEventsChannelFlow) {
        when (it) {
            NavigateBack -> onNavigateUp.invoke()
        }
    }

    LaunchedEffect(Unit) {
        setOnCheckClicked {
            viewModel.addItem()
        }
    }

    CreateItemScreenContent(
        state = createScreenState,
        onNameChanged = {
            viewModel.updateTextInput(it)
        },
        onCategorySelected = {
            viewModel.updateCategoryInput(it)
        },
        onDescriptionChanged = {
            viewModel.updateDescription(it)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateItemScreenContent(
    modifier: Modifier = Modifier,
    state: CreateItemScreenState = CreateItemScreenState(),

    onNameChanged: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onDescriptionChanged: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        StatefulOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Name")
            },
            onValueChange = onNameChanged,
            isError = state.nameErrorText.isNotBlank(),
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = Color.Red
            )
        )
        if (state.nameErrorText.isNotBlank()) {
            Text(
                text = state.nameErrorText,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 2.dp),
                textAlign = TextAlign.Start
            )
        }
        Spacer(modifier = Modifier.padding(5.dp))
        StatefulOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Description (optional)")
            },
            onValueChange = onDescriptionChanged,
            isError = state.nameErrorText.isNotBlank(),
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = Color.Red
            )
        )
        Spacer(modifier = Modifier.padding(10.dp))
        FlowRow(
            Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight(align = Alignment.Top),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Category.entries.filterNot { it == Category.OTHER}.forEach { category ->
                FilterChip(
                    selected = category == state.category,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .align(alignment = Alignment.CenterVertically),
                    onClick = {
                        onCategorySelected(category)
                    },
                    leadingIcon = {
                        Image(
                            modifier = Modifier.padding(2.dp),
                            painter = painterResource(id = category.drawable),
                            contentDescription = ""
                        )
                    },
                    label = {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(top = 6.dp),
                            text = stringResource(id = category.text)
                        )
                    })
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CreateItemScreenPreview(

) {
    ShopitoTheme {
        CreateItemScreenContent(
            modifier = Modifier.padding(12.dp),
            state = CreateItemScreenState(),
            onNameChanged = {},
            onCategorySelected = {},
            onDescriptionChanged = {}
        )
    }
}