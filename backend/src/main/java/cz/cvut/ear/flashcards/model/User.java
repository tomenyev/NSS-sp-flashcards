package cz.cvut.ear.flashcards.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.*;


/**
 * @author Tomen
 */
@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email") })
@NamedQuery(name = "User.findAllUsers", query = "SELECT e FROM User e")
public class User extends AbstractEntity implements UserDetails {


    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("updatedat DESC")
    @JoinTable(
            name = "topic_users",
            joinColumns = @JoinColumn(
                    name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "topic_id", referencedColumnName = "id"))
    private Set<Topic> topics = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @JsonProperty("role")
    private String role() {
        for(Role r : roles) {
            return r.getName().toString();
        }
        return null;
    }

    @JsonIgnore
    @OrderBy("updatedat DESC")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Review> reviews;

    private boolean active;

    public User(@NotNull String username, @NotNull String email, @NotNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public User(@NotNull String username, @NotNull @Email String email, @NotNull String password, Set<Topic> topics, Set<Role> roles, Set<Review> reviews, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.topics = topics;
        this.roles = roles;
        this.reviews = reviews;
        this.active = active;
    }

    public User() { }

    public static class Builder {

        private String username;

        private String email;

        private String password;

        private Set<Topic> topics = new HashSet<>();

        private Set<Role> roles = new HashSet<>();

        private Set<Review> reviews;

        private boolean active = true;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withTopics(Set<Topic> topics) {
            this.topics = topics;
            return this;
        }

        public Builder withRoles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder withReviews(Set<Review> reviews) {
            this.reviews = reviews;
            return this;
        }

        public Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public User build() {
            return new User( username, email, password, topics, roles, reviews, active);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void addTopic(Topic topic) { topics.add(topic); }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return active == user.active &&
//                Objects.equals(username, user.username) &&
//                Objects.equals(email, user.email) &&
//                Objects.equals(password, user.password) &&
//                Objects.equals(topics, user.topics) &&
//                Objects.equals(roles, user.roles) &&
//                Objects.equals(reviews, user.reviews);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(username, email, password, topics, roles, reviews, active);
//    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", active=" + active +
                '}';
    }

}
