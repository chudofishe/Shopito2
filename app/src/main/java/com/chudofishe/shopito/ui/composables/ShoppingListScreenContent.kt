package com.chudofishe.shopito.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.R
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.ui.shoppinglist.CompleteAnimation
import com.chudofishe.shopito.ui.shoppinglist.ShoppingListEntries
import com.chudofishe.shopito.ui.shoppinglist.categorisedList

@Composable
fun ShoppingListScreenContent(
    modifier: Modifier = Modifier,
    list: ShoppingList,
    showCompleteAnimation: Boolean = false,

    onItemRemoveButtonClicked: (ShoppingListItem) -> Unit = {},
    onItemChecked: (ShoppingListItem) -> Unit = {},
    onCategoryAddButtonClicked: (Category) -> Unit = {},
    onCategoryCollapseStateToggled: (Category) -> Unit = {},
    onAnimationFinished: () -> Unit = {}
) {
    val entries: ShoppingListEntries = remember(list.items) {
        list.toCategorisedMap()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            categorisedList(
                entries = entries,
                collapsedCategories = list.collapsedCategories,
                completedCategories = list.completedCategories,
                isReadOnly = true,
                onItemChecked = { item ->
                    onItemChecked(item)
                },
                onItemRemoveButtonClicked = onItemRemoveButtonClicked,
                onCollapsedButtonClicked = onCategoryCollapseStateToggled,
                onAddButtonClicked = onCategoryAddButtonClicked,
            )
        }
    }

    if (entries.isEmpty()) {
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
