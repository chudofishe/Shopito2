package com.chudofishe.shopito.util


sealed class UIEvent {
    data object ShowCompleteAnimationEvent : UIEvent()
    data class ShowToastEvent(val text: String) : UIEvent()
}
