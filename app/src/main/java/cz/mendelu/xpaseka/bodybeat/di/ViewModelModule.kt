package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.PlansViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PlansViewModel(get())
    }
}