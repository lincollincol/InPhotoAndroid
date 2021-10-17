package com.linc.inphoto.ui.splash

sealed class SplashEvent {
    object CheckLoggedInEvent : SplashEvent()
}