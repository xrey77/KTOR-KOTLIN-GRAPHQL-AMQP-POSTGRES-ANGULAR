// src/main/kotlin/com/api/graphql/queries/getProductSearch.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import com.api.model.ProductModel
import com.repositories.ProductTable
import com.api.model.PagedResponse
import graphql.GraphQLException 

import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.SqlExpressionBuilder.concat
import org.jetbrains.exposed.sql.stringLiteral
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.lowerCase

class GetProductSearch : Query {

    suspend fun getProductsearch(page: Int, keyword: String): PagedResponse {

        val pageSize = 5
        val calculatedOffset = ((page - 1) * pageSize).toLong()

        val lowercaseKeyword = keyword.lowercase()        

        val (totalRecords, data) = transaction {
            val query = ProductTable.selectAll().where { 
                ProductTable.descriptions.lowerCase() like concat(stringLiteral("%"), stringLiteral(lowercaseKeyword), stringLiteral("%")) 
            }

            val total = query.count() 
            
            if (total == 0L) {
                throw GraphQLException("No products found in the database.")
            }

            val records = query.copy()
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
// query GetProductsearch($page: Int!, $keyword: String!) {
//   getProductsearch(page: $page, keyword: $keyword) {
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
//   "page": 1,
//   "keyword": "X500"
// }
