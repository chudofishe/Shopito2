package com.chudofishe.shopito.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.model.getCategoryToItemsList
import com.chudofishe.shopito.ui.home.current_shoppinglist.CategoryCard
import com.chudofishe.shopito.ui.home.current_shoppinglist.EmptyListPreview

@Composable
fun ShoppingListScreenContent(
    modifier: Modifier = Modifier,
    list: ShoppingList,
    collapsedCategories: Set<Category> = emptySet(),
    showCompleteAnimation: Boolean = false,
    isReadOnly: Boolean = false,

    onItemRemoveButtonClicked: (ShoppingListItem) -> Unit = {},
    onItemClicked: (ShoppingListItem) -> Unit = {},
    onCategoryAddButtonClicked: (Category) -> Unit = {},
    onCategoryCollapseStateToggled: (Category) -> Unit = {},
    onAnimationFinished: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(list.items.getCategoryToItemsList()) {
                CategoryCard(
                    category = it.first,
                    items = it.second,
                    isReadOnly = isReadOnly,
                    isCollapsed = it.first in collapsedCategories,
                    onItemRemoved = onItemRemoveButtonClicked,
                    onItemClicked = onItemClicked,
                    onCollapsedButtonClicked = onCategoryCollapseStateToggled,
                    onAddButtonClicked = onCategoryAddButtonClicked
                )
            }
        }
    }

    if (list.items.isEmpty()) {
        EmptyListPreview(
            showCompleteAnimation = showCompleteAnimation,
            onAnimationFinished = onAnimationFinished
        )
    }
}
