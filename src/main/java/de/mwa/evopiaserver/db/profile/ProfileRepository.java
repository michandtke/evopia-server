package de.mwa.evopiaserver.db.profile;

import de.mwa.evopiaserver.db.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
