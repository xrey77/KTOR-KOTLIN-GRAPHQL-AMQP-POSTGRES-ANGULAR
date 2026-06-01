// src/main/kotlin/api/model/product.kt
package com.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal


@Serializable
data class ProductModel(
    val id: Int,
    val descriptions: String,
    val qty: Int,
    val unit: String,
    val costprice: Double, 
    val sellprice: Double,
    val productpicture: String,
)


@Serializable
data class PagedResponse(
    val page: Int,
    val totalPages: Int,
    val totalRecords: Int,
    val products: List<ProductModel>
)

@Serializable
data class Product(
    val id: Int,
    val descriptions: String,
    val qty: Int,
    val unit: String,
    val costprice: Double, 
    val sellprice: Double,
    val saleprice: Double, 
    val productpicture: String,
    val alertstocks: Int,
    val criticalstocks: Int, 
    val createdAt: String, 
    val updatedAt: String, 

    val category: Category
)

@Serializable
data class CategoryWithProducts(
    val categoryName: String,
    val products: List<ProductDetail>
)

@Serializable
data class ProductDetail(
    val id: Int,
    val descriptions: String,
    val qty: Int,
    val unit: String?,
    val costprice: Double,
    val sellprice: Double
)