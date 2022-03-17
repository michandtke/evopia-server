package de.mwa.evopiaserver.registration;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Setter
@NoArgsConstructor
@ToString
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfRegistration;
    private String email;
    private String password;
    private String imagePath;

    public User(Long id, String firstName, String lastName, String dateOfRegistration, String email, String password, String imagePath) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
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
                && Objects.equals(imagePath, user.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dateOfRegistration, email, password, imagePath);
    }
}
