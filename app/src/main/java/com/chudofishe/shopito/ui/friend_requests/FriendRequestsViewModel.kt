package com.chudofishe.shopito.ui.friend_requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRepository
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRequestRepository
import com.chudofishe.shopito.model.UserData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FriendRequestsViewModel(
    private val firebaseFriendRequestRepository: FirebaseFriendRequestRepository,
    private val firebaseFriendRepository: FirebaseFriendRepository
) : ViewModel() {

    private val _friendRequests = MutableStateFlow<List<UserData>>(emptyList())
    val friendRequests = _friendRequests.asStateFlow()

    private val toastChannel = Channel<String>()
    val toastChannelFlow = toastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            firebaseFriendRequestRepository.getFriendRequests().collect { result ->
                if (result is RealtimeDatabaseValueResult.Success) {
                    _friendRequests.value = result.data
                }
            }
        }
    }

    fun declineFriendRequest(request: UserData) {
        viewModelScope.launch {
            when (val res = firebaseFriendRequestRepository.deleteFriendRequestByUid(request.userId)) {
                is RealtimeDatabaseResult.Error -> toastChannel.trySend(res.error.toString())
                RealtimeDatabaseResult.Success -> toastChannel.trySend("Request declined")
                RealtimeDatabaseResult.Loading -> {}
            }
        }
    }

    fun acceptFriendRequest(request: UserData) {
        viewModelScope.launch {
            val deleteResult = firebaseFriendRequestRepository.deleteFriendRequestByUid(request.userId)
            if (deleteResult is RealtimeDatabaseResult.Success) {
                when (val addRes = firebaseFriendRepository.addFriend(request)) {
                    is RealtimeDatabaseResult.Error -> toastChannel.trySend(addRes.error.toString())
                    RealtimeDatabaseResult.Success -> toastChannel.trySend("Friend added")
                    RealtimeDatabaseResult.Loading -> {}
                }
            } else {
                toastChannel.trySend("Request not found")
            }
        }
    }
}