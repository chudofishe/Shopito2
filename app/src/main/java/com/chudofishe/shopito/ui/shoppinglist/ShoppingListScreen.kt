package com.chudofishe.shopito.ui.shoppinglist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.chudofishe.shopito.util.ObserveAsEvents
import com.chudofishe.shopito.util.UIEvent
import com.chudofishe.shopito.model.Category
import org.koin.androidx.compose.koinViewModel
import com.chudofishe.shopito.ui.composables.ShoppingListScreenContent


@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    onNavigateToAddItemScreenWithCategory: (Category) -> Unit
) {

    val viewmodel: ShoppingListScreenViewModel = koinViewModel()
    val currentList by viewmodel.currentList.collectAsState()

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
        list = currentList,
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




