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
import com.shopsmart.app.features.home.data.repository.HomeRepositoryImpl
import com.shopsmart.app.features.home.data.repository.ProductRepositoryImpl
import com.shopsmart.app.features.home.domain.repository.HomeRepository
import com.shopsmart.app.features.home.domain.repository.ProductRepository
import com.shopsmart.app.features.home.domain.usecase.GetBannersUseCase
import com.shopsmart.app.features.home.domain.usecase.GetCategoriesUseCase
import com.shopsmart.app.features.home.domain.usecase.GetFeaturedProductsUseCase
import com.shopsmart.app.features.home.domain.usecase.GetProductByIdUseCase
import com.shopsmart.app.features.home.domain.usecase.GetRelatedProductsUseCase
import com.shopsmart.app.features.home.presentation.viewmodels.HomeViewModel
import com.shopsmart.app.features.home.presentation.viewmodels.ProductDetailViewModel
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

val homeModule = module {
  single<HomeRepository> { HomeRepositoryImpl() }

  factory { GetCategoriesUseCase(get()) }
  factory { GetFeaturedProductsUseCase(get()) }
  factory { GetBannersUseCase(get()) }

  viewModel {
    HomeViewModel(
      getCategoriesUseCase = get(),
      getFeaturedProductsUseCase = get(),
      getBannersUseCase = get(),
    )
  }
}


val productModule = module {
  single<ProductRepository> { ProductRepositoryImpl() }

  factory { GetProductByIdUseCase(get()) }
  factory { GetRelatedProductsUseCase(get()) }

  viewModel {
    ProductDetailViewModel(
      getProductByIdUseCase = get(),
      getRelatedProductsUseCase = get(),
    )
  }
}
