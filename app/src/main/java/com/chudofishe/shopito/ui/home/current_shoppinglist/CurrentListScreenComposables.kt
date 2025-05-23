package com.chudofishe.shopito.ui.home.current_shoppinglist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.chudofishe.shopito.R
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import java.time.LocalDateTime


@Composable
fun CompleteAnimation(
    onAnimationFinished: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_complete))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition
    )

    LaunchedEffect(progress) {
        if (progress == 1f) onAnimationFinished()
    }
}

@Composable
fun EmptyListPreview(
    showCompleteAnimation: Boolean = false,
    onAnimationFinished: () -> Unit = {}
) {
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

@Composable
fun CategoryHeader(
    category: Category,
    isCategoryCompleted: Boolean = false,
    isReadOnly: Boolean = false,

    onCollapsedButtonClicked: (Category) -> Unit = {},
    onAddButtonClicked: (Category) -> Unit
) {

    val addButtonModifier =
        Modifier
            .alpha(0.5f)
            .clickable {
                onAddButtonClicked.invoke(category)
            }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCollapsedButtonClicked(category)
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(42.dp),
            painter = painterResource(id = category.drawable),
            contentDescription = "Category image"
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .basicMarquee()
                .alpha(
                    if (isCategoryCompleted) 0.3f else 1f
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = stringResource(id = category.text),
            style = MaterialTheme.typography.headlineSmall,
            textDecoration = if (isCategoryCompleted) TextDecoration.LineThrough else TextDecoration.None
        )
        if (!isReadOnly) {
            Icon(
                modifier = addButtonModifier,
                imageVector = Icons.Default.Add,
                contentDescription = ""
            )
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItem(
    modifier: Modifier = Modifier,
    item: ShoppingListItem,
    isReadOnly: Boolean = false,
    onItemClicked: (ShoppingListItem) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable {
                onItemClicked(item)
            }
            .padding(
                12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
            )
        }
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    category: Category,
    items: List<ShoppingListItem>,
    isReadOnly: Boolean = false,
    isCollapsed: Boolean = false,
    onCollapsedButtonClicked: (Category) -> Unit = {},
    onAddButtonClicked: (Category) -> Unit = {},
    onItemRemoved: (ShoppingListItem) -> Unit = {},
    onItemClicked: (ShoppingListItem) -> Unit = {}
) {
    Card(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            CategoryHeader(
                category = category,
                isReadOnly = isReadOnly,
                onCollapsedButtonClicked = onCollapsedButtonClicked,
                onAddButtonClicked = onAddButtonClicked
            )
            if (!isCollapsed) {
                Column {
                    items.forEach {item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    onItemRemoved(item)
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 20. dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(Icons.Default.Close, "Remove")
                                }
                            }
                        ) {
                            ShoppingItem(
                                item = item,
                                isReadOnly = isReadOnly,
                                onItemClicked = onItemClicked,
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true,
    backgroundColor = 0xFF888888)
fun CategoryCardPreview(

) {
    val items = generateShoppingListItems().toMutableList()

    ShopitoTheme {
        CategoryCard(
            modifier = Modifier.padding(12.dp),
            category = Category.MEAT,
            items = items,
            onItemRemoved = {
                items.remove(it)
            }
        )
    }
}


@Composable
@Preview(showBackground = true)
fun ShoppingItemPreview(

) {
    ShopitoTheme {
        ShoppingItem(
            item = ShoppingListItem(
                name = "Test",
                category = Category.BAKERY,
                currentCategory = Category.BAKERY,
                timeStamp = LocalDateTime.now(),
                description = "Description"
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CategoryHeaderPreview(

) {
    ShopitoTheme {
        CategoryHeader(Category.MEAT) {}
    }
}

private fun generateShoppingListItems(numItems: Int = 5): List<ShoppingListItem> {
    val categories = listOf(Category.MEAT, Category.DRINKS, Category.BAKERY) // Replace with your actual Category enum
    return List(numItems) { index ->
        ShoppingListItem(
            id = index + 1,
            name = "Item $index",
            category = categories[index % categories.size], // Cycle through the categories
            currentCategory = categories[index % categories.size],
            timeStamp = LocalDateTime.now(),
        )
    }
}

