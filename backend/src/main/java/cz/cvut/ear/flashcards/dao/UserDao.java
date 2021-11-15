package cz.cvut.ear.flashcards.dao;

import cz.cvut.ear.flashcards.model.User;
import org.springframework.stereotype.Repository;

/** User DAO
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Repository
public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(User.class);
    }

    /** Check if username exists
     * @param username User name
     * @return boolean If username exists
    */
    public boolean existsUsername(String username) {
        return this.findAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    /** Check if email exists
     * @param email User email
     * @return boolean If email exists
    */
    public boolean existsEmail(String email) {
        return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

}
