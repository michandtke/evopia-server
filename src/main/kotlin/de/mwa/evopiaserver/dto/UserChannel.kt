package de.mwa.evopiaserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserChannel(val name: String, val value: String)