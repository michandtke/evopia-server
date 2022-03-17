package de.mwa.evopiaserver.registration;

import de.mwa.evopiaserver.db.profile.Profile;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@NoArgsConstructor
@ToString
@Table(name = "useraccount")
public class User {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfRegistration;
    private String email;
    private String password;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    public User(Long id, String firstName, String lastName, String dateOfRegistration, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(dateOfRegistration, user.dateOfRegistration)
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && Objects.equals(profile, user.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dateOfRegistration, email, password, profile);
    }
}
