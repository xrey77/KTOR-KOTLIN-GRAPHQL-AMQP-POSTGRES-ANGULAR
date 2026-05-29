package com.api.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivateMfaDto(
    val twofactorenabled: Boolean,
)