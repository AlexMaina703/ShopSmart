package com.shopsmart.app.features.home.domain.usecase


import com.shopsmart.app.features.home.domain.model.Banner
import com.shopsmart.app.features.home.domain.repository.HomeRepository

class GetBannersUseCase(private val repository: HomeRepository) {
    operator fun invoke(): List<Banner> = repository.getLocalBanners()
}