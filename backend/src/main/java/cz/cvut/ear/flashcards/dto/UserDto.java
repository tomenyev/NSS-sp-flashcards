package cz.cvut.ear.flashcards.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


/** User DTO (Data Transfer Object)
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
public class UserDto {

    @NotEmpty
    private String username;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String repassword;


    // getters, setters
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

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
}
