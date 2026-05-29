package com.api.model

import java.math.BigDecimal
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.* 
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

data class ProductRow(
    val id: Int,
    val descriptions: String,
    val qty: Int,
    val unit: String,
    val costprice: BigDecimal,
    val sellprice: BigDecimal,
    val saleprice: BigDecimal,
    val productpicture: String,
    val alertstocks: Int,   
    val criticalstocks: Int,
    val categoryId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

object Categories : Table("categories") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    
    override val primaryKey = PrimaryKey(id, name = "pk_categories_id")
}

object Products : Table("products") {
    val id = integer("id").autoIncrement()
    val descriptions = varchar("descriptions", length = 100).uniqueIndex("uq_products_descriptions")
    val qty = integer(name="qty").default(0)
    val unit = varchar("unit", length = 255).nullable()
    val costprice = decimal("costprice", 10, 2).default(BigDecimal.ZERO)
    val sellprice = decimal("sellprice", 10, 2).default(BigDecimal.ZERO)
    val saleprice = decimal("saleprice", 10, 2).default(BigDecimal.ZERO)
    val productpicture = varchar("productpicture", length = 255).nullable()
    val alertstocks = integer(name="alertstocks").default(0)    
    val criticalstocks = integer(name="criticalstocks").default(0)
    
    val categoryId = integer("category_id")
        .references(Categories.id, fkName = "fk_products_category_id")         

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)    
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
    
    override val primaryKey = PrimaryKey(id, name = "pk_products_id")
}
