package com.chudofishe.shopito.ui.manage_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRequestRepository
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRepository
import com.chudofishe.shopito.model.FriendData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val firebaseFriendRequestRepository: FirebaseFriendRequestRepository,
    private val firebaseFriendRepository: FirebaseFriendRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FriendsScreenState())
    val state = _state.asStateFlow()

    private val toastChanel = Channel<String>()
    val toastChannelFlow = toastChanel.receiveAsFlow()

    init {
        viewModelScope.launch {
            firebaseFriendRequestRepository.getFriendRequests()
                .combine(firebaseFriendRepository.getFriends()) { requests, friends ->
                    _state.update {
                        it.copy(
                            friends = if (friends is RealtimeDatabaseValueResult.Success) {
                                friends.data
                            } else {
                                emptyList()
                            },
                            friendRequests = if (requests is RealtimeDatabaseValueResult.Success) {
                                requests.data
                            } else {
                                emptyList()
                            }
                        )
                    }
                }
        }
    }

    fun sendFriendRequest(email: String) {
        viewModelScope.launch {
            val res = firebaseFriendRequestRepository.sendFriendRequest(email)
            when (res) {
                is RealtimeDatabaseResult.Error -> {
                    toastChanel.trySend("Failed to send request")
                }
                RealtimeDatabaseResult.Loading -> TODO()
                RealtimeDatabaseResult.Success -> {
                    toastChanel.trySend("Request sent")
                }
            }
        }
    }

    fun declineFriendRequest(request: FriendData) {
        viewModelScope.launch {
            val res = firebaseFriendRequestRepository.deleteFriendRequestByUid(request.uid)
            when (res) {
                is RealtimeDatabaseResult.Error -> {
                    toastChanel.trySend(res.error.toString())
                }
                RealtimeDatabaseResult.Loading -> TODO()
                RealtimeDatabaseResult.Success -> {
                    toastChanel.trySend("Request declined")
                }
            }
        }
    }

    fun acceptFriendRequest(request: FriendData) {
        viewModelScope.launch {
            val res = firebaseFriendRequestRepository.deleteFriendRequestByUid(request.uid)
            if (res is RealtimeDatabaseResult.Success) {
                val addRes = firebaseFriendRepository.addFriend(request.uid)
                when (addRes) {
                    is RealtimeDatabaseResult.Error -> {
                        toastChanel.trySend(addRes.error.toString())
                    }
                    RealtimeDatabaseResult.Loading -> TODO()
                    RealtimeDatabaseResult.Success -> {
                        toastChanel.trySend("Friend added")
                    }
                }
            } else {
                toastChanel.trySend("Request not found")
            }
        }
    }

    fun removeFriend(friendData: FriendData) {
        viewModelScope.launch {
            val res = firebaseFriendRepository.removeFriend(friendData.uid)
            when(res) {
                is RealtimeDatabaseResult.Error -> {
                    toastChanel.trySend(res.error.toString())
                }
                RealtimeDatabaseResult.Loading -> TODO()
                RealtimeDatabaseResult.Success -> {
                    toastChanel.trySend("Friend removed")
                }
            }
        }
    }
}

data class FriendsScreenState(
    val friends: List<FriendData> = emptyList(),
    val friendRequests: List<FriendData> = emptyList()
)