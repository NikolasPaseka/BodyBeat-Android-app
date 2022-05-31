package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PlansViewModel(get())
    }
    viewModel {
        NewPlanViewModel(get())
    }
    viewModel{
        PlanDetailViewModel(get())
    }
    viewModel {
        PlanProgressViewModel(get())
    }
    viewModel {
        ScheduleViewModel(get(), get())
    }
    viewModel {
        ParksViewModel()
    }
    viewModel {
        ParkDetailViewModel()
    }
    viewModel {
        ParkMapViewModel()
    }
}