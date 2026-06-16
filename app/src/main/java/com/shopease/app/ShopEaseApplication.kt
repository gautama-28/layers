package com.shopease.app

import android.app.Application
import com.shopease.app.di.AppContainer

class ShopEaseApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
