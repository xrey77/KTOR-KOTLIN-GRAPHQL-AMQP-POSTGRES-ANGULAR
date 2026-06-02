// src/main/kotlin/com/api/graphql/queries/getSalesData.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.api.model.SaleModel
import graphql.GraphQLException 
import com.repositories.SalesRepositoryImpl
import com.services.SalesService

class SalesDataQuery : Query {
    
    private val salesRepository = SalesRepositoryImpl()
    private val salesService = SalesService(salesRepository)

    suspend fun getSalesdata(): List<SaleModel> {

        val data = newSuspendedTransaction {
            salesService.salesDataList()
        }

        return data
    }    
}

// REQUEST
// query GetSalesdata {
//   getSalesdata {
//     salesamount
//     salesdate
//   }
// }
