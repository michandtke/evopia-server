package de.mwa.evopiaserver.api.dto

import kotlinx.serialization.*

@Serializable
data class EventDto(
    val id: Int = -1,
    val name: String,
    val description: String,
    val from: String,
    val to: String,
    val place: String,
    val imagePath: String,
    val tags: List<TagDto>
)
