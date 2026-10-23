package com.example.lab10_2

import android.app.Application
import com.example.lab10_2.data.AppContainer
import com.example.lab10_2.data.DefaultAppContainer

class BooksApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}