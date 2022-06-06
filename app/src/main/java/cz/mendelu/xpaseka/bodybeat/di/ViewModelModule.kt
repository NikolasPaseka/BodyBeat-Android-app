package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.fragments.ScheduleViewModel
import cz.mendelu.xpaseka.bodybeat.fragments.parks.*
import cz.mendelu.xpaseka.bodybeat.fragments.plans.NewPlanViewModel
import cz.mendelu.xpaseka.bodybeat.fragments.plans.PlanDetailViewModel
import cz.mendelu.xpaseka.bodybeat.fragments.plans.PlanProgressViewModel
import cz.mendelu.xpaseka.bodybeat.fragments.plans.PlansViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PlansViewModel(get(), get())
    }
    viewModel {
        NewPlanViewModel(get(), get(), get())
    }
    viewModel{
        PlanDetailViewModel(get())
    }
    viewModel {
        PlanProgressViewModel(get(), get())
    }
    viewModel {
        ScheduleViewModel(get(), get(), get())
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
    viewModel {
        UploadParkViewModel()
    }
    viewModel {
        SelectLocationViewModel()
    }
}