// src/main/kotlin/com/services/ProductService.kt
package com.services

import com.api.model.ProductModel
import com.api.model.CategoryWithProducts
import com.api.model.PagedResponse
import com.repositories.ProductRepositoryImpl

class ProductService(private val productRepository: ProductRepositoryImpl) {

    suspend fun productDataList(page: Int): PagedResponse {
        val response = productRepository.findProducts(page)
        if (response.products.isEmpty()) {
            throw IllegalArgumentException("Products not found.")
        }
        return response
    }    

    suspend fun productDataSearch(page: Int, descriptions: String): PagedResponse {
        val response = productRepository.searchProducts(page, descriptions)
        if (response.products.isEmpty()) {
            throw IllegalArgumentException("Products not found.")
        }
        return response
    }    

    suspend fun getCategoriesWithProducts(): List<CategoryWithProducts> {
        return productRepository.getCategoriesWithProducts()
    }
}