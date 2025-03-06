package com.chudofishe.shopito


sealed class UIEvent {
    data object ShowCompleteAnimationEvent : UIEvent()
    data class ShowToastEvent(val text: String) : UIEvent()
}
