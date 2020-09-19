package srawat.eng.mishi_challenge.di

import srawat.eng.mishi_challenge.coreComponent
import srawat.eng.mishi_challenge.ui.HomeActivity

fun inject(activity: HomeActivity) {
    DaggerHomeComponent.builder()
        .coreComponent(activity.coreComponent())
        .homeModule(HomeModule(activity))
        .build()
        .inject(activity)
}