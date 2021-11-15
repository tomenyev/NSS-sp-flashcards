package cz.cvut.ear.flashcards.repository;

import cz.cvut.ear.flashcards.model.User;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
    HashSet<User> findAll();

    Set<User> findAllByUsernameContainingIgnoreCase(@NotNull String username);

    Set<User> findAllByEmailContainingIgnoreCase(@NotNull String email);

    Set<User> findAllByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(@NotNull String username, @NotNull String email);
}
