package com.chudofishe.shopito.ui.home.current_shoppinglist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
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



@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun LazyListScope.categorisedList(
    entries: ShoppingListEntries,
    collapsedCategories: Set<Category>,
    completedCategories: Set<Category>,
    isReadOnly: Boolean = false,

    onItemRemoveButtonClicked: (ShoppingListItem) -> Unit,
    onItemChecked: (item: ShoppingListItem) -> Unit,
    onCollapsedButtonClicked: (Category) -> Unit,
    onAddButtonClicked: (Category) -> Unit
) {
    entries.keys.forEach { category ->
        stickyHeader {
            CategoryHeader(
                category = category,
                isReadOnly = isReadOnly,
                onCollapsedButtonClicked = onCollapsedButtonClicked,
                onAddButtonClicked = onAddButtonClicked,
                isCategoryCompleted = category in completedCategories
            )
            HorizontalDivider(
                thickness = 1.dp
            )
        }
        if (!collapsedCategories.contains(category)) {
            entries[category]?.let { items ->
                items(items, key = { it.name + it.id }) {
                    ShoppingItem(
                        modifier = Modifier.animateItem(),
                        item = it,
                        isReadOnly = isReadOnly,
                        onDeleteButtonClicked = {
                            onItemRemoveButtonClicked(it)
                        },
                        onCheckedChanged = {
                            onItemChecked(it)
                        }
                    )
                }
            }
        }
    }
}

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
            .background(color = MaterialTheme.colorScheme.surface)
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

    onDeleteButtonClicked: () -> Unit = {},
    onCheckedChanged: () -> Unit ={}
) {
    val deleteButtonModifier = Modifier
        .alpha(0.3f)
        .clickable {
            onDeleteButtonClicked.invoke()
        }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChanged()
            }
            .padding(
                12.dp
            ),
        verticalAlignment = Alignment.Top
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = {
                    onCheckedChanged()
                }
            )
        }
        Column(
            modifier = Modifier
                .alpha(
                    if (item.isChecked) 0.3f else 1f
                )
                .padding(start = 12.dp, end = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = item.name,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )
            if (item.description.isNotBlank()) {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    fontStyle = FontStyle.Italic,
                    text = item.description
                )
            }
        }
        if (!isReadOnly) {
            Icon(
                modifier = deleteButtonModifier,
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
        }
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
                isChecked = false,
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

