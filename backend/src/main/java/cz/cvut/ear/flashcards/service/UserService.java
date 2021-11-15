package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.repository.UserRepository;
import cz.cvut.ear.flashcards.dto.SearchDto;
import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User service class
 */
@Service
@SuppressWarnings("ALL")
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicService topicService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private CardService cardService;

    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * get user by username
     * @param s user's username
     * @return user object
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userFindByUserName = userRepository.findByUsername(s);
        User userFindByEmail = userRepository.findByEmail(s);

        if(userFindByUserName != null)
        {
            return userFindByUserName;
        }

        if(userFindByEmail != null)
        {
            return userFindByEmail;
        }

        return null;
    }

    /**
     * update user by user id
     * @param userId user id
     * @param newUser new user entity
     * @return updated user
     */
    public User updateUser(Integer userId, User newUser) {
        int i = 0;
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            if (!user.getUsername().equals(newUser.getUsername())
                    && newUser.getUsername() != null&&
                    userRepository.findByUsername(newUser.getUsername()) == null) {
                i++;
                user.setUsername(newUser.getUsername());
            }
            if (!user.getEmail().equals(newUser.getEmail()) &&
                    newUser.getEmail() != null &&
                    userRepository.findByEmail(newUser.getEmail()) == null) {
                i++;
                user.setEmail(newUser.getEmail());
            }
            if (newUser.getPassword() != null) {
                String pass = new BCryptPasswordEncoder().encode(newUser.getPassword());
                if(!user.getPassword().equals(pass)) {
                    i++;
                    user.setPassword(pass);
                }
            }

            if(i != 0) {
                return userRepository.save(user);
            }
        }

        return null;

    }

    /**
     * create topic and add to user's topics by user id
     * @param userId user id
     * @param topic new user topic
     * @return updated user
     */
    public User addTopic(Integer userId, Topic topic) {
        User user = getUser(userId);
        if (topic.getAuthor() == null || topic.getAuthor().isEmpty()) {
            topic.setAuthor(user.getUsername());
        }
        topic = topicService.addTopic(topic);
        user.addTopic(topic);
        return userRepository.save(user);
    }

    /**
     * add topic to user's topics by topic id and user id
     * @param topicId topic id
     * @param userId user id
     * @return updated user
     */
    public User addTopic(Integer topicId, Integer userId) {
        User user = this.getUser(userId);
        Topic topic = topicService.getTopic(topicId);

        user.addTopic(topic);

        user = userRepository.save(user);

        return user;
    }

    /**
     * clone topic and add cloned topic to user's topics
     * @param topicId topic id
     * @param userId user id
     * @return cloned topic
     */
    public Topic copyTopic(Integer topicId, Integer userId) {

        User user = this.getUser(userId);

        Topic topicToCopy = topicService.getTopic(topicId);

        Topic topic = topicToCopy.clone();

        topic.setAuthor(user.getUsername());

        topic = topicService.addTopic(topic);

        for (Deck d : topicToCopy.getDecks()) {
            Deck deck = d.clone();
            deck.setAuthor(user.getUsername());
            deck = deckService.addDeck(topic.getId(), deck);
            for (Card c : d.getCards()) {
                cardService.addCard(c.clone(), deck.getId());
            }
        }

        user.addTopic(topic);

        return topic;

    }

    /**
     * delete user by id
     * @param userId user id
     */
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    /**
     * get user by id
     * @param userId user id
     * @return user entity
     */
    public User getUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User not found! ID: " + userId);
        }
        return userRepository.findById(userId).get();
    }

    /**
     * unsubscribe user from public topic
     * @param topicId topic id
     * @param userId user id
     * @return updated user
     */
    public User unsubscribeFromPublicTopic(Integer topicId, Integer userId) {
        User user = this.getUser(userId);
        Set<Topic> topics = user.getTopics();
        user.setTopics(
                topics.stream()
                        .filter(topic -> !topic.getId().equals(topicId))
                        .collect(Collectors.toSet())
        );
        user = userRepository.save(user);
        return user;
    }

    /**
     * create user
     * @param user user entity
     * @return created user
     */
    public User addUser(User user) {
        if (user != null) {
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * parse search request from searchDao to Map<String, Set<String>>
     * @param searchDto searchDto
     * @return Map<String, Set<String>> as parsed search request
     */
    public Map<String, Set<String>> parseSearchReqest(SearchDto searchDto) {
        String request = searchDto.getSearch();

        if(request == null || request.isEmpty()) {
            return null;
        }

        StringBuilder username = new StringBuilder("");

        Set<String> usernames = new HashSet<>();

        char[] array = request.toCharArray();

        for(int i = 0; i < array.length; i++) {
            boolean add = false;
            switch (array[i]) {
                case '@':
                    continue;
                default:
                    add = false;
                    for(; i < array.length; i++) {
                        char c = array[i];
                        if(Character.isLetter(c) || Character.isDigit(c)) {
                            username.append(c);
                            add = true;
                        } else {
                            if(c == ' ') {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        usernames.add(username.toString());
                        username = new StringBuilder("");
                    }
                    break;
                case ' ':
                    continue;
                case '#':
                    continue;
                case '\n':
                    continue;
            }
        }

        Map<String, Set<String>> map = new HashMap<>();

        map.put("usernames", usernames);

        return map;
    }

    /**
     * search users by parsed search request
     * @param map parsed search reqeust
     * @return set of users
     */
    public Set<User> search(Map<String, Set<String>> map) {

        Set<String> usernames = map.get("usernames");


        if(!usernames.isEmpty()) {
            return usernames
                    .stream()
                    .map(userRepository::findAllByUsernameContainingIgnoreCase)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }


        return new HashSet<>();
    }

}
