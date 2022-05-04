package de.mwa.evopiaserver.dto

data class UserProfile(val imagePath: String, val tags: List<UserTag>, val profileChannels: List<UserChannel>)