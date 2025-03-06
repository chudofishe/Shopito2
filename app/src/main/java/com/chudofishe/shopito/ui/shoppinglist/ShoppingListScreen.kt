package com.chudofishe.shopito.ui.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.ObserveAsEvents
import com.chudofishe.shopito.UIEvent
import com.chudofishe.shopito.areAllItemsComplete
import com.chudofishe.shopito.getCompletedCategories
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.navigation.BottomNavigationRoute
import org.koin.androidx.compose.koinViewModel
import com.chudofishe.shopito.R


@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    onNavigateToAddItemScreenWithCategory: (Category) -> Unit
) {

    val viewmodel: ShoppingListScreenViewModel = koinViewModel()
    val screenState by viewmodel.state.collectAsState()

    var showCompleteAnimation by remember { mutableStateOf(false) }

    ObserveAsEvents(viewmodel.uiEventChannelFlow) {
        when (it) {
            UIEvent.ShowCompleteAnimationEvent -> {
                showCompleteAnimation = true
            }
            else -> {

            }
        }
    }

    ShoppingListScreenContent(
        modifier = modifier,
        state = screenState,
        showCompleteAnimation = showCompleteAnimation,
        onItemRemoveButtonClicked = {
            viewmodel.removeItem(it)
        },
        onItemChecked = {
            viewmodel.updateItem(it.copy(
                isChecked = !it.isChecked
            ))
        },
        onCategoryAddButtonClicked = onNavigateToAddItemScreenWithCategory,
        onCategoryCollapseStateToggled = {
            viewmodel.updateCollapsedCategory(it)
        },
        onAnimationFinished = {
            showCompleteAnimation = false
        }
    )
}


@Composable
fun ShoppingListScreenContent(
    modifier: Modifier = Modifier,
    state: ShoppingListScreenState,
    showCompleteAnimation: Boolean,

    onItemRemoveButtonClicked: (ShoppingListItem) -> Unit,
    onItemChecked: (ShoppingListItem) -> Unit,
    onCategoryAddButtonClicked: (Category) -> Unit,
    onCategoryCollapseStateToggled: (Category) -> Unit,
    onAnimationFinished: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            categorisedList(
                entries = state.entries,
                collapsedCategories = state.collapsedCategories,
                completedCategories = state.completedCategories,
                onItemChecked = { item ->
                    onItemChecked(item)
                },
                onItemRemoveButtonClicked = onItemRemoveButtonClicked,
                onCollapsedButtonClicked = onCategoryCollapseStateToggled,
                onAddButtonClicked = onCategoryAddButtonClicked,
            )
        }
    }

    if (state.isListEmpty) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (showCompleteAnimation) {
                CompleteAnimation(
                    onAnimationFinished = onAnimationFinished
                )
            } else {
                Text(text = stringResource(id = R.string.label_list_empty))
            }
        }
    }
}


