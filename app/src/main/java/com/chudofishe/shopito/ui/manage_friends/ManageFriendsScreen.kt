package com.chudofishe.shopito.ui.manage_friends

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private data class TabItem(
    val title: String
)

private val tabItem = listOf(
    TabItem(
        title = "Friends"
    ), TabItem(
        title = "Requests"
    )
)

@Composable
fun ManageFriendsScreen(

) {

    val profileViewModel: FriendsViewModel = koinViewModel()
    val state by profileViewModel.state.collectAsState()

    ManageFriendsScreenContent(
        state
    )
}

@Composable
fun ManageFriendsScreenContent(
    state: FriendsScreenState
) {
    val pagerState = rememberPagerState {
        tabItem.size
    }

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            selectedTabIndex = pagerState.currentPage
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn {
            items(items = state.friends) {

            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FriendsListContentPreview(

) {
    ManageFriendsScreenContent(
        state = FriendsScreenState()
    )
}

