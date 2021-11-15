package cz.cvut.ear.flashcards.repository;

import cz.cvut.ear.flashcards.model.Deck;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.HashSet;

public interface DeckRepository extends CrudRepository<Deck, Integer> {
    HashSet<Deck> findAll();

    HashSet<Deck> findAllByAuthorAndNameContains(@NotNull String author, @NotNull String name);

}