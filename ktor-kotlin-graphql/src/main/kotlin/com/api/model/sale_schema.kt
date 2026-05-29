package com.api.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
import java.math.BigDecimal
import java.time.Instant
import org.jetbrains.exposed.sql.javatime.date 
import org.jetbrains.exposed.sql.* 

data class SaleRow(
    val id: Int,             
    val salesamount: BigDecimal,
    val salesdate: Instant,
    val createdAt: Instant,
    val updatedAt: Instant
)

object Sales : Table("sales") {
    val id = integer("id").autoIncrement()
    val salesamount = decimal("salesamount", 10, 2).default(BigDecimal.ZERO)
    val salesdate = timestamp("salesdate")

    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)    
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    
    override val primaryKey = PrimaryKey(id)
}
