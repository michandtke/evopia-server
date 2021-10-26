package de.mwa.evopiaserver.registration;

public interface IUserService {
    User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistsException;
}
