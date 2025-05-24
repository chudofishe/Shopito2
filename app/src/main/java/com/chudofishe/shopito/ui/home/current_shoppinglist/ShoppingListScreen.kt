package com.chudofishe.shopito.ui.home.current_shoppinglist

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.ui.composables.ShoppingListScreenContent


@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    shoppingList: ShoppingList,
    collapsedCategories: Set<Category>,

    onNavigateToAddItemScreenWithCategory: (Category) -> Unit,
    onItemRemoved: (ShoppingListItem) -> Unit,
    onItemClicked: (ShoppingListItem) -> Unit,
    onCategoryCollapseStateToggled: (Category) -> Unit
) {

    ShoppingListScreenContent(
        modifier = modifier.padding(
            horizontal = 12.dp
        ),
        list = shoppingList,
        collapsedCategories = collapsedCategories,

        onItemRemoveButtonClicked = onItemRemoved,
        onItemClicked = onItemClicked,
        onCategoryAddButtonClicked = onNavigateToAddItemScreenWithCategory,
        onCategoryCollapseStateToggled = onCategoryCollapseStateToggled,
    )
}




