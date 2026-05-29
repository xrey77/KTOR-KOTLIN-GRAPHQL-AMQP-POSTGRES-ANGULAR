package com.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val id: Int,
    val name: String,
)
