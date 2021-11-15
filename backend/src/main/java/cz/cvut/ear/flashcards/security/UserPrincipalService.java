package cz.cvut.ear.flashcards.security;

import cz.cvut.ear.flashcards.dao.UserDao;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserPrincipalService implements UserDetailsService {

    private final UserDao userDao;

    public UserPrincipalService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findAll().stream().filter(us -> us.getUsername().equals(username)).findFirst().orElseThrow(() -> new UsernameNotFoundException("User not found: "+ username));

        return UserPrincipal.create(user);
    }


}
