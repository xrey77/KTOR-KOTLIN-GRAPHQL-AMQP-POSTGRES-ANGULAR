// src/main/kotlin/com/api/graphql/queries/getMasterDetails.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.api.model.CategoryWithProducts
import graphql.GraphQLException 
import com.repositories.ProductRepositoryImpl
import com.services.ProductService

class ProductMasterDetailsQuery : Query {
    
    private val productRepository = ProductRepositoryImpl()
    private val productService = ProductService(productRepository)

    suspend fun getProductMasterDetails(): List<CategoryWithProducts> {

        val data = newSuspendedTransaction {
            productService.getCategoriesWithProducts()
        }

        return data
    }    
}

// REQUEST
// query {
//   getProductMasterDetails {
//     categoryName
//     products {
//       id
//       descriptions
//       qty
//       unit
//       costprice
//       sellprice      
//     }
//   }
// }
