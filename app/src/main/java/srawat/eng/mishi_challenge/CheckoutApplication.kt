package srawat.eng.mishi_challenge

import android.app.Activity
import android.app.Application
import android.content.Context
import srawat.eng.core.CoreComponent
import srawat.eng.core.DaggerCoreComponent

class CheckoutApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.create()
    }

    companion object {
        @JvmStatic
        fun coreComponent(context: Context) =
            (context.applicationContext as CheckoutApplication).coreComponent
    }
}

fun Activity.coreComponent() = CheckoutApplication.coreComponent(this)