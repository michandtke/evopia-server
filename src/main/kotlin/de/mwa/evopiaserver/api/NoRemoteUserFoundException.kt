package de.mwa.evopiaserver.api

import java.lang.RuntimeException

class NoRemoteUserFoundException(message: String?) : RuntimeException(message)