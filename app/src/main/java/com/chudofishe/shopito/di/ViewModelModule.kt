package com.chudofishe.shopito.di

import com.chudofishe.shopito.ui.RootViewModel
import com.chudofishe.shopito.ui.add_list_item.create_item.CreateItemScreenViewModel
import com.chudofishe.shopito.ui.add_list_item.recent_Items.RecentItemScreenViewModel
import com.chudofishe.shopito.ui.friend_requests.FriendRequestsViewModel
import com.chudofishe.shopito.ui.friends.FriendsViewModel
import com.chudofishe.shopito.ui.home.HomeViewModel
import com.chudofishe.shopito.ui.profile.ProfileViewModel
import com.chudofishe.shopito.ui.recent_lists.RecentShoppingListsViewmodel
import com.chudofishe.shopito.ui.shoppinglist.ShoppingListScreenViewModel
import com.chudofishe.shopito.ui.shoppinglistview.ShoppingListScreenViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { ShoppingListScreenViewModel(get()) }
    viewModel { RecentItemScreenViewModel(get(), get()) }
    viewModel { CreateItemScreenViewModel(get(), get(), get()) }
    viewModel { RecentShoppingListsViewmodel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ShoppingListScreenViewViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { RootViewModel(get(), get()) }
    viewModel { FriendsViewModel(get(), get()) }
    viewModel {
        FriendRequestsViewModel(
            firebaseFriendRequestRepository = get(),
            firebaseFriendRepository = get()
        )
    }
}