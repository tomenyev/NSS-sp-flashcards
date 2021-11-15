package cz.cvut.ear.flashcards.repository;

import cz.cvut.ear.flashcards.model.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;

public interface CardRepository extends CrudRepository<Card, Integer> {

    @Query("SELECT e FROM Card e INNER JOIN e.decks v WHERE v.id in :deckId")
    HashSet<Card> findAllByDeckId(@Param("deckId") int deckId);
}