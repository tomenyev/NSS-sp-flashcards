package cz.cvut.ear.flashcards.dao;

import cz.cvut.ear.flashcards.model.Card;
import org.springframework.stereotype.Repository;

/** Card DAO
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Repository
public class CardDao extends BaseDao<Card> {
    public CardDao() {
        super(Card.class);
    }
}
