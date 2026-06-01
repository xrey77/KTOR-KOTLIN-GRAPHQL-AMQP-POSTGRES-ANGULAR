package com.kotlin.services

import com.api.model.Sale
import com.api.model.SaleModel
import com.repositories.SalesRepositoryImpl

class SalesService(private val salesRepository: SalesRepositoryImpl) {

    suspend fun salesDataList(): List<SaleModel> {
        val response = salesRepository.findSales()
        if (response.isEmpty()) {
            throw IllegalArgumentException("Sales not found.")
        }
        return response
    }    
}
