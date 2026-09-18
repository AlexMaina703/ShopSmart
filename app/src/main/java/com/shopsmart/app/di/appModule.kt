package com.shopsmart.app.di
import com.shopsmart.app.core.datastore.DataStoreManager
import com.shopsmart.app.features.auth.data.repository.AuthRepositoryImpl
import com.shopsmart.app.features.auth.domain.repository.AuthRepository
import com.shopsmart.app.features.auth.domain.usecase.ForgotPasswordUseCase
import com.shopsmart.app.features.auth.domain.usecase.LoginUseCase
import com.shopsmart.app.features.auth.domain.usecase.OnboardingUseCase
import com.shopsmart.app.features.auth.domain.usecase.RegisterUseCase
import com.shopsmart.app.features.auth.domain.usecase.ResetPasswordUseCase
import com.shopsmart.app.features.auth.domain.usecase.SocialLoginUseCase
import com.shopsmart.app.features.auth.domain.usecase.ValidateTokenUseCase
import com.shopsmart.app.features.auth.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
  single { DataStoreManager(get()) }
  single<AuthRepository> { AuthRepositoryImpl(get()) }

  factory { LoginUseCase(get()) }
  factory { RegisterUseCase(get()) }
  factory { SocialLoginUseCase(get()) }
  factory { ValidateTokenUseCase(get()) }
  factory { OnboardingUseCase(get()) }
  factory { ForgotPasswordUseCase(get()) }
  factory { ResetPasswordUseCase(get()) }

  viewModel {
    AuthViewModel(
      loginUseCase = get(),
      registerUseCase = get(),
      socialLoginUseCase = get(),
      validateTokenUseCase = get(),
      onboardingUseCase = get(),
      forgotPasswordUseCase = get(),
      resetPasswordUseCase = get()
    )
  }
}
