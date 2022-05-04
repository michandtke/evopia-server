package de.mwa.evopiaserver.registration

import java.lang.RuntimeException

class UserAlreadyExistsException(message: String?) : RuntimeException(message)