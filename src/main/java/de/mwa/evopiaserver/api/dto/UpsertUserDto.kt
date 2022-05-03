package de.mwa.evopiaserver.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpsertUserDto(val imagePath: String)