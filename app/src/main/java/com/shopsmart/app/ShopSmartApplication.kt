package com.shopsmart.app

import android.app.Application
import com.shopsmart.app.di.authModule
import com.shopsmart.app.di.cartModule
import com.shopsmart.app.di.categoriesModule
import com.shopsmart.app.di.homeModule
import com.shopsmart.app.di.notificationModule
import com.shopsmart.app.di.orderModule
import com.shopsmart.app.di.productModule
import com.shopsmart.app.di.profileModule
import com.shopsmart.app.di.searchModule
import com.shopsmart.app.di.settingsModule
import com.shopsmart.app.di.wishlistModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopSmartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopSmartApplication)
            modules(
                authModule,
                homeModule,
                productModule,
                categoriesModule,
                cartModule,
                orderModule,
                profileModule,
                wishlistModule,
                notificationModule,
                settingsModule,
                searchModule,
            )
        }
    }
}