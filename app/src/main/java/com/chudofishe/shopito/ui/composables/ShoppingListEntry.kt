package com.chudofishe.shopito.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import java.time.LocalDateTime

@Composable
fun ShoppingListEntry(
    modifier: Modifier = Modifier,
    shoppingListItem: ShoppingListItem
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = shoppingListItem.category.drawable),
            contentDescription = "Category image"
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = shoppingListItem.name,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListEntryPreview(

) {
    ShopitoTheme {
        ShoppingListEntry(
            modifier = Modifier.padding(12.dp),
            ShoppingListItem(
                name = "Steak",
                category = Category.MEAT,
                timeStamp = LocalDateTime.now(),
                currentCategory = Category.MEAT,
            )
        )
    }
}