// src/main/kotlin/com/api/graphql/queries/getProductList.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import com.api.model.ProductModel
import com.repositories.ProductTable
import com.api.model.PagedResponse
import graphql.GraphQLException 

class ProductListQuery : Query {
    fun getProductlist(page: Int): PagedResponse {

        val pageSize = 5
        val calculatedOffset = ((page - 1) * pageSize).toLong()
        
        val (totalRecords, data) = transaction {
            val total = ProductTable.selectAll().count() 
            
            if (total == 0L) {
                throw GraphQLException("No products found in the database.")
            }

            val records = ProductTable
                .selectAll()
                .limit(pageSize, offset = calculatedOffset)
                .map { row ->
                    ProductModel(
                        id = row[ProductTable.id],
                        descriptions = row[ProductTable.descriptions],
                        qty = row[ProductTable.qty],                        
                        unit = row[ProductTable.unit],
                        costprice = row[ProductTable.costprice],
                        sellprice = row[ProductTable.sellprice],
                        productpicture = row[ProductTable.productpicture],
                    )
                }
            
            Pair(total, records)
        }

        val totalPages = kotlin.math.ceil(totalRecords.toDouble() / pageSize).toInt()

        if (data.isEmpty()) {
            throw GraphQLException("No products found in the database.")
        }

        return PagedResponse(
            page = page,
            totalPages = totalPages,
            totalRecords = totalRecords.toInt(),
            products = data
        )
    }
}


// REQUEST
// query GetProductlist($page: Int!) {
//   getProductlist(page: $page) {
//     page
//     totalPages
//     totalRecords
//     products {
//       id
//       descriptions
//       qty
//       unit
//       costprice
//       sellprice
//       productpicture
//     }
//   }
// }


// VARIABLES
// {
//   "page":1
// }
