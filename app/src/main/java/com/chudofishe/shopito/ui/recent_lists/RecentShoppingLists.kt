package com.chudofishe.shopito.ui.recent_lists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.util.ObserveAsEvents
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute.HomeRoute
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute.ViewListRoute
import com.chudofishe.shopito.ui.friends.AddFriendDialog
import com.chudofishe.shopito.ui.friends.FriendsScreenContent
import com.chudofishe.shopito.util.toDayOfWeekDateTimeString
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import com.chudofishe.shopito.util.createSampleShoppingList
import com.chudofishe.shopito.util.isScrollingUp
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentShoppingListsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToViewList: (Long) -> Unit,
    onNavigateToHome: (showDrawer: Boolean) -> Unit
) {
    val viewmodel: RecentShoppingListsViewmodel = koinViewModel()
    val state by viewmodel.state.collectAsState()
    val listState = rememberLazyListState()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    ObserveAsEvents(viewmodel.navigationEventsChannelFlow) {
        when (it) {
            is ViewListRoute -> {
                onNavigateToViewList(it.listId)
            }
            is HomeRoute -> {
                onNavigateToHome(false)
            }
            else -> { /* Игнорируем другие события */ }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Recent lists")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                scrollBehavior = topBarScrollBehavior
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = listState.isScrollingUp().value,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth } // выезд справа
                ) + fadeIn(),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth } // уезжает вправо
                ) + fadeOut()
            )  {
                FloatingActionButton(
                    onClick = {
                        viewmodel.addNewList()
                    }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Add list")
                }
            }
        }
    ) { padding ->
        RecentShoppingListsContent(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            state = state,
            listState = listState,
            onItemClicked = {
                viewmodel.navigateToViewList(it.id)
            }
        )
    }
}

@Composable
fun RecentShoppingListsContent(
    modifier: Modifier = Modifier,
    state: RecentShoppingListsScreenState,
    listState: LazyListState = rememberLazyListState(),
    onItemClicked: (ShoppingList) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = listState,
    ) {
        state.currentList?.let {
            item {
                ShoppingListPreview(
                    modifier = Modifier.padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    ),
                    list = it,
                    onClick = onItemClicked
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = 12.dp,
                    )
                )
            }
        }
        items(state.items.filter { it != state.currentList }) {
            ShoppingListPreview(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp
                ),
                list = it,
                onClick = onItemClicked
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RecentShoppingListsContentPreview() {
    ShopitoTheme {
        RecentShoppingListsContent(
            state = RecentShoppingListsScreenState(
                items = List(5) { index -> createSampleShoppingList(index.toLong() + 2) },
                currentList = createSampleShoppingList(1)
            ),
            listState = rememberLazyListState(),
            onItemClicked = { }
        )
    }
}

@Composable
fun ShoppingListPreview(
    modifier: Modifier = Modifier,
    list: ShoppingList,
    onClick: (ShoppingList) -> Unit
) {
    val completedCount = list.items.count { it.currentCategory == Category.COMPLETED }

    Card(
        modifier = modifier
            .clickable { onClick(list) }
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                    text = list.timestamp.toDayOfWeekDateTimeString(),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = {
                        completedCount.toFloat() / list.items.size.toFloat()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .weight(1f),
                    drawStopIndicator = {}
                )
                if (list.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                    )
                } else {
                    Text(
                        text = "$completedCount/${list.items.size}"
                    )
                }
            }
            Row {
                list.getCategories().forEach {
                    Image(
                        modifier = Modifier.size(32.dp),
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
            modifier = Modifier.padding(24.dp),
            list = createSampleShoppingList(),
            onClick = {

            }
        )
    }
}



