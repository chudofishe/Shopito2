package com.chudofishe.shopito.ui.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.ObserveAsEvents
import com.chudofishe.shopito.UIEvent
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import org.koin.androidx.compose.koinViewModel
import com.chudofishe.shopito.R
import com.chudofishe.shopito.model.ShoppingList
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




