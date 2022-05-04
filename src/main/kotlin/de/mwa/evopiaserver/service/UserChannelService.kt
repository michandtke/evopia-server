package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.service

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.UserChannel
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.UserChannelRepositoryNew

class UserChannelService(private val userChannelRepository: UserChannelRepositoryNew) {

    fun getChannels(email: String): List<UserChannel> {
        return userChannelRepository.findForUser(email)
    }

    fun add(remoteUser: String, channels: List<UserChannel>): Pair<Int, Int> {
        return userChannelRepository.add(remoteUser, channels)
    }
}
