package com.majorproject.roomify.core.di

import com.majorproject.roomify.feature.auth.data.repo_impl.AuthRepositoryImpl
import com.majorproject.roomify.feature.auth.domain.repo.AuthRepository
import com.majorproject.roomify.feature.category_detail.data.repoimpl.CategoryProductRepositoryImpl
import com.majorproject.roomify.feature.category_detail.domain.repo.CategoryProductRepository
import com.majorproject.roomify.feature.furniture_list.data.repo_impl.CategoryRepositoryImpl
import com.majorproject.roomify.feature.furniture_list.data.repo_impl.ProductRepositoryImpl
import com.majorproject.roomify.feature.furniture_list.domain.repo.CategoryRepository
import com.majorproject.roomify.feature.furniture_list.domain.repo.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCategoryProductRepository(
        repositoryImpl: CategoryProductRepositoryImpl
    ): CategoryProductRepository
}