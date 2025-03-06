package com.chudofishe.shopito.ui.add_list_item.recent_Items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.ui.composables.ShoppingListEntry
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun RecentItemsScreen(
    onItemClicked: (ShoppingListItem) -> Unit
) {
    val viewModel: RecentItemScreenViewModel = koinViewModel()
    val state by viewModel.items.collectAsState()

    RecentItemsScreenContent(
        items = state,
        onItemClicked = {
            onItemClicked(it)
        }
    )
}

@Composable
fun RecentItemsScreenContent(
    modifier: Modifier = Modifier,
    items: List<ShoppingListItem>,

    onItemClicked: (ShoppingListItem) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LazyColumn {
            items(
                items = items
            ) { item ->
                Box(
                    modifier = Modifier.clickable { onItemClicked(item) }
                ) {
                    ShoppingListEntry(
                        modifier = Modifier.padding(12.dp),
                        shoppingListItem = item
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecentItemsScreenContentPreview(

) {
    ShopitoTheme {
        RecentItemsScreenContent(
            modifier = Modifier,
            items = generateShoppingListItems(),
            onItemClicked = {

            }
        )
    }
}

private fun generateShoppingListItems(): List<ShoppingListItem> {
    val categories = listOf(Category.MEAT, Category.DRINKS, Category.BAKERY) // Replace with your actual Category enum
    return List(15) { index ->
        ShoppingListItem(
            id = index + 1,
            name = "Item $index",
            category = categories[index % categories.size], // Cycle through the categories
            timeStamp = LocalDateTime.now(),
            isChecked = false
        )
    }
}

