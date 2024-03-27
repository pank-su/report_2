package domain.di

import domain.SetImageColorsUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        SetImageColorsUseCase()
    }
}