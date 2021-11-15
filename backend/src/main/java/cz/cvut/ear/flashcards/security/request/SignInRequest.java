package cz.cvut.ear.flashcards.security.request;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


@Entity
public class SignInRequest {

    @Id
    private Integer id;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    public SignInRequest() {


    }

    public SignInRequest(@NotBlank String login, @NotBlank String password) {
        this.login = login;
        this.password = password;
    }

    public String getUsername() {
        return login;
    }

    public void setUsername(String username) {
        this.login = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
