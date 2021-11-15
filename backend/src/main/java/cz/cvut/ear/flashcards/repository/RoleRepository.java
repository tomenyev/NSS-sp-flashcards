package cz.cvut.ear.flashcards.repository;

import cz.cvut.ear.flashcards.model.ERole;
import cz.cvut.ear.flashcards.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);

}
