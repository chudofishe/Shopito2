package com.chudofishe.shopito.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRepository
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRequestRepository
import com.chudofishe.shopito.model.FriendData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val firebaseFriendRepository: FirebaseFriendRepository,
    private val firebaseFriendRequestRepository: FirebaseFriendRequestRepository
) : ViewModel() {

    private val _friends = MutableStateFlow<List<FriendData>>(emptyList())
    val friends = _friends.asStateFlow()

    private val toastChannel = Channel<String>()
    val toastChannelFlow = toastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            firebaseFriendRepository.getFriends().collect { result ->
                if (result is RealtimeDatabaseValueResult.Success) {
                    _friends.value = result.data
                }
            }
        }
    }

    fun removeFriend(friendData: FriendData) {
        viewModelScope.launch {
            when (val res = firebaseFriendRepository.removeFriend(friendData.uid)) {
                is RealtimeDatabaseResult.Error -> toastChannel.trySend(res.error.toString())
                RealtimeDatabaseResult.Success -> toastChannel.trySend("Friend removed")
                RealtimeDatabaseResult.Loading -> {}
            }
        }
    }

    fun sendFriendRequest(email: String) {
        viewModelScope.launch {
            when (val res = firebaseFriendRequestRepository.sendFriendRequest(email)) {
                is RealtimeDatabaseResult.Error -> toastChannel.trySend("Failed to send request")
                RealtimeDatabaseResult.Success -> toastChannel.trySend("Request sent")
                RealtimeDatabaseResult.Loading -> {}
            }
        }
    }
}