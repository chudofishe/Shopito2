package com.chudofishe.shopito.ui.recent_lists

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.ObserveAsEvents
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.navigation.BottomNavigationRoute
import com.chudofishe.shopito.toDayOfWeekDateTimeString
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun RecentShoppingListsScreen(
    modifier: Modifier = Modifier,
    setRecentListsFabOnClick: (() -> Unit) -> Unit,
    onNavigateToCurrentList: () -> Unit
) {
    val viewmodel: RecentShoppingListsViewmodel = koinViewModel()
    val state by viewmodel.state.collectAsState()

    ObserveAsEvents(viewmodel.navigationEventsChannelFlow) {
        when (it) {
            BottomNavigationRoute.CurrentListRoute -> onNavigateToCurrentList()
        }
    }

    LaunchedEffect(Unit) {
        setRecentListsFabOnClick {
            viewmodel.addNewList()
        }
    }

    RecentShoppingListsContent(
        modifier = modifier,
        state = state,
        onItemClicked = {
            viewmodel.setCurrentListId(it.id)
        }
    )
}

@Composable
fun RecentShoppingListsContent(
    modifier: Modifier = Modifier,
    state: RecentShoppingListsScreenState,
    onItemClicked: (ShoppingList) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(

        ) {
            state.currentList?.let {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            fontStyle = FontStyle.Italic,
                            text = "Current"
                        )
                    }
                    ShoppingListPreview(
                        list = it,
                        onClick = onItemClicked
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            fontStyle = FontStyle.Italic,
                            text = "Recent"
                        )
                    }
                }
            }

            items(state.items) {
                ShoppingListPreview(
                    list = it,
                    onClick = onItemClicked
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ShoppingListPreview(
    list: ShoppingList,
    onClick: (ShoppingList) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick(list) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                if (list.isCompleted) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(32.dp),
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                    text = list.timestamp.toDayOfWeekDateTimeString(),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Row(
                modifier = Modifier
                    .alpha(0.7f)
            ) {
                Text(
                    text = "${list.items.size} items",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "${list.items.count { it.isChecked }} checked",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row {
                list.getCategories().forEach {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = it.drawable),
                        contentDescription = it.name
                    )
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun ShoppingListPreviewPreview(

) {
    ShopitoTheme {
        ShoppingListPreview(
            list = createSampleShoppingList(),
            onClick = {

            }
        )
    }
}

//@Composable
//@Preview(showBackground = true)
//fun RecentShoppingListsPreview(
//
//) {
//    ShopitoTheme {
//        RecentShoppingListsContent(
//            state = RecentShoppingListsScreenState(
//                items = listOf(createSampleShoppingList(), createSampleShoppingList(), createSampleShoppingList()),
//                currentList = createSampleShoppingList()
//            ),
//             onItemClicked = {
//
//             }
//        )
//    }
//}
//
private fun createSampleShoppingList(): ShoppingList {
    val item1 = ShoppingListItem(
        id = 0, // Auto-generated by the database, placeholder here
        name = "Apples",
        category = Category.FRUITS_AND_VEGETABLES, // Assuming Category is an enum or another class you have
        timeStamp = LocalDateTime.now(),
        isChecked = false
    )

    val item2 = ShoppingListItem(
        id = 0, // Auto-generated by the database, placeholder here
        name = "Shampoo",
        category = Category.HYGIENE_AND_COSMETICS,
        timeStamp = LocalDateTime.now(),
        isChecked = true
    )

    val item3 = ShoppingListItem(
        id = 0, // Auto-generated by the database, placeholder here
        name = "Bread",
        category = Category.BAKERY,
        timeStamp = LocalDateTime.now(),
        isChecked = false
    )

    return ShoppingList(
        id = 1, // You can modify this as needed
        items = listOf(item1, item2, item3),
        timestamp = LocalDateTime.now(),
        isCompleted = true
    )
}
