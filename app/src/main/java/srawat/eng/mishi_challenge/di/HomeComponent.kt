package srawat.eng.mishi_challenge.di

import dagger.Component
import srawat.eng.core.BaseActivityComponent
import srawat.eng.core.CoreComponent
import srawat.eng.core.FeatureScope
import srawat.eng.mishi_challenge.ui.HomeActivity

@Component(
    modules = [
        DataModule::class,
        HomeModule::class
    ],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface HomeComponent : BaseActivityComponent<HomeActivity> {
    @Component.Builder
    interface Builder {
        fun build(): HomeComponent
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun homeModule(homeModule: HomeModule): Builder
        fun dataModule(dataModule: DataModule): Builder
    }
}