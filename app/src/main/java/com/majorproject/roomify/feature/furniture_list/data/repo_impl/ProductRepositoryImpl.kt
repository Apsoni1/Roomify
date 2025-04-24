package com.majorproject.roomify.feature.furniture_list.data.repo_impl


import com.majorproject.roomify.feature.furniture_list.data.remote_data_source.ProductsRemoteDataSource
import com.majorproject.roomify.feature.furniture_list.domain.repo.ProductRepository
import com.majorproject.roomify.feature.furniture_list.presentation.adapter.ProductPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val remote: ProductsRemoteDataSource
): ProductRepository {

    override fun pagingSource(): ProductPagingSource {
        return ProductPagingSource { page, limit ->
            remote.fetchProducts(page, limit)
        }
    }
}


