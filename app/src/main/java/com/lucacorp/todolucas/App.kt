package com.lucacorp.todolucas

import android.app.Application
import com.lucacorp.todolucas.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.setUpContext(this)
    }
}