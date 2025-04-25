package com.chudofishe.shopito.ui.friend_requests

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

class FriendRequestsViewModel(
    private val firebaseFriendRequestRepository: FirebaseFriendRequestRepository,
    private val firebaseFriendRepository: FirebaseFriendRepository
) : ViewModel() {

    private val _friendRequests = MutableStateFlow<List<FriendData>>(emptyList())
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

    fun sendFriendRequest(email: String) {
        viewModelScope.launch {
            when (val res = firebaseFriendRequestRepository.sendFriendRequest(email)) {
                is RealtimeDatabaseResult.Error -> toastChannel.trySend("Failed to send request")
                RealtimeDatabaseResult.Success -> toastChannel.trySend("Request sent")
                RealtimeDatabaseResult.Loading -> {}
            }
        }
    }

    fun declineFriendRequest(request: FriendData) {
        viewModelScope.launch {
            when (val res = firebaseFriendRequestRepository.deleteFriendRequestByUid(request.uid)) {
                is RealtimeDatabaseResult.Error -> toastChannel.trySend(res.error.toString())
                RealtimeDatabaseResult.Success -> toastChannel.trySend("Request declined")
                RealtimeDatabaseResult.Loading -> {}
            }
        }
    }

    fun acceptFriendRequest(request: FriendData) {
        viewModelScope.launch {
            val deleteResult = firebaseFriendRequestRepository.deleteFriendRequestByUid(request.uid)
            if (deleteResult is RealtimeDatabaseResult.Success) {
                when (val addRes = firebaseFriendRepository.addFriend(request.uid)) {
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