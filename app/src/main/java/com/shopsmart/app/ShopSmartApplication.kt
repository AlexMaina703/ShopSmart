package com.shopsmart.app

import android.app.Application
import com.shopsmart.app.di.authModule
import com.shopsmart.app.di.cartModule
import com.shopsmart.app.di.categoriesModule
import com.shopsmart.app.di.homeModule
import com.shopsmart.app.di.orderModule
import com.shopsmart.app.di.productModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopSmartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopSmartApplication)
            modules(authModule, homeModule, productModule, categoriesModule, cartModule, orderModule)        }
    }
}