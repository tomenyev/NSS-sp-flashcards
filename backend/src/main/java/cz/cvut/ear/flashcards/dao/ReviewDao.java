package cz.cvut.ear.flashcards.dao;

import cz.cvut.ear.flashcards.model.Review;
import org.springframework.stereotype.Repository;

/** Review DAO
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Repository
public class ReviewDao extends BaseDao<Review> {

    public ReviewDao() {
        super(Review.class);
    }
}
