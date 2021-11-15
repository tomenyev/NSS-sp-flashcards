package cz.cvut.ear.flashcards.security.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

public class SignUpRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    private List<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRole() {
        return this.role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }
}
