package cz.cvut.ear.flashcards.dao;

import cz.cvut.ear.flashcards.model.Deck;
import org.springframework.stereotype.Repository;

/** Deck DAO
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Repository
public class DeckDao extends BaseDao<Deck> {
    public DeckDao() {
        super(Deck.class);
    }
}
