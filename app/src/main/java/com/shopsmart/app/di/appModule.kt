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
import com.shopsmart.app.features.cart.data.repository.CartRepositoryImpl
import com.shopsmart.app.features.home.data.repository.HomeRepositoryImpl
import com.shopsmart.app.features.home.data.repository.ProductRepositoryImpl
import com.shopsmart.app.features.home.domain.repository.HomeRepository
import com.shopsmart.app.features.home.domain.repository.ProductRepository
import com.shopsmart.app.features.home.domain.usecase.GetBannersUseCase
import com.shopsmart.app.features.home.domain.usecase.GetCategoriesUseCase
import com.shopsmart.app.features.home.domain.usecase.GetFeaturedProductsUseCase
import com.shopsmart.app.features.home.domain.usecase.GetProductByIdUseCase
import com.shopsmart.app.features.home.domain.usecase.GetProductsUseCase
import com.shopsmart.app.features.home.domain.usecase.GetRelatedProductsUseCase
import com.shopsmart.app.features.home.presentation.viewmodels.CategoriesViewModel
import com.shopsmart.app.features.home.presentation.viewmodels.HomeViewModel
import com.shopsmart.app.features.home.presentation.viewmodels.ProductDetailViewModel
import com.shopsmart.app.features.home.presentation.viewmodels.ProductListViewModel
import com.shopsmart.app.features.cart.domain.repository.CartRepository
import com.shopsmart.app.features.order.data.repository.OrderRepositoryImpl
import com.shopsmart.app.features.order.domain.repository.OrderRepository
import com.shopsmart.app.features.order.domain.usecase.*
import com.shopsmart.app.features.order.presentation.viewmodels.CheckoutViewModel
import com.shopsmart.app.features.order.presentation.viewmodels.OrdersViewModel
import com.shopsmart.app.features.cart.domain.usecase.*
import com.shopsmart.app.features.cart.presentation.viewmodels.CartViewModel
import com.shopsmart.app.features.order.presentation.viewmodels.AddAddressViewModel
import com.shopsmart.app.features.order.presentation.viewmodels.AddPaymentMethodViewModel
import com.shopsmart.app.features.order.presentation.viewmodels.OrderDetailViewModel
import com.shopsmart.app.features.order.presentation.viewmodels.TrackOrderViewModel
import com.shopsmart.app.features.profile.data.repository.ProfileRepositoryImpl
import com.shopsmart.app.features.profile.domain.repository.ProfileRepository
import com.shopsmart.app.features.profile.domain.usecase.GetProfileUseCase
import com.shopsmart.app.features.profile.domain.usecase.UpdateProfileUseCase
import com.shopsmart.app.features.profile.presentation.viewmodel.EditProfileViewModel
import com.shopsmart.app.features.profile.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module
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
        addToCartUseCase = get(),
    )
  }
}

val categoriesModule = module {
  factory { GetProductsUseCase(get()) }

  viewModel { CategoriesViewModel(get(), get()) }
  viewModel { ProductListViewModel(get()) }
}


val cartModule = module {
  single<CartRepository> { CartRepositoryImpl() }

  factory { GetCartUseCase(get()) }
  factory { AddToCartUseCase(get()) }
  factory { UpdateCartItemUseCase(get()) }
  factory { RemoveCartItemUseCase(get()) }
  factory { ClearCartUseCase(get()) }

  viewModel {
    CartViewModel(
      getCartUseCase = get(),
      updateCartItemUseCase = get(),
      removeCartItemUseCase = get(),
      clearCartUseCase = get(),
    )
  }
}


val orderModule = module {
  single<OrderRepository> { OrderRepositoryImpl() }

  factory { GetAddressesUseCase(get()) }
  factory { GetPaymentMethodsUseCase(get()) }
  factory { PlaceOrderUseCase(get()) }
  factory { GetOrdersUseCase(get()) }
  factory { GetOrderByIdUseCase(get()) }
  factory { AddAddressUseCase(get()) }
  factory { DeleteAddressUseCase(get()) }
  factory { AddPaymentMethodUseCase(get()) }
  factory { DeletePaymentMethodUseCase(get()) }
  factory { GetOrderTrackingUseCase(get()) }

  viewModel {
    CheckoutViewModel(
      getAddressesUseCase = get(),
      getPaymentMethodsUseCase = get(),
      placeOrderUseCase = get(),
      clearCartUseCase = get(),
    )
  }
  viewModel { OrdersViewModel(get()) }
  viewModel { OrderDetailViewModel(get()) }
  viewModel { AddAddressViewModel(get()) }
  viewModel { AddPaymentMethodViewModel(get()) }
  viewModel { TrackOrderViewModel(get()) }
}

val profileModule = module {
  single<ProfileRepository> { ProfileRepositoryImpl() }

  factory { GetProfileUseCase(get()) }
  factory { UpdateProfileUseCase(get()) }

  viewModel { ProfileViewModel(get(), get()) }
  viewModel { EditProfileViewModel(get()) }
}