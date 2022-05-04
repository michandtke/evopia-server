package de.mwa.evopiaserver.api

import java.lang.RuntimeException

class UnknownChannelException(message: String?) : RuntimeException(message)