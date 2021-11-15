package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.esdao.EsUserDao;
import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.repository.RoleRepository;
import cz.cvut.ear.flashcards.repository.UserRepository;
import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.model.User;
import cz.cvut.ear.flashcards.dto.SearchDto;
import cz.cvut.ear.flashcards.model.*;
import cz.cvut.ear.flashcards.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private CardService cardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImportExportFacade importExportFacade;

    @Autowired
    private EsUserDao userDao;
    
    @Autowired
    private SearchFacade searchFacade;

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * Get all users
     *
     * @return HashSet<User>
     */
    @GetMapping
    public Set<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Get single user
     *
     * @param userId
     * @return User
     */
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Get single user's topics
     *
     * @param userId
     * @return HashSet<Topic>
     */
    @GetMapping("/{userId}/topics")
    public Set<Topic> getUserTopics(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null)
            return user.getTopics();
        else
            return new HashSet<>();
    }

    /**
     * Get single user's decks
     *
     * @param userId
     * @return HashSet<Deck>
     */
    @GetMapping("/{userId}/decks")
    public Set<Deck> getUserDecks(@PathVariable Integer userId) {
        return this.deckService.getUserDecks(userId);
    }

    /**
     * Get single user's cards
     *
     * @param userId
     * @return User
     */
    @GetMapping("/{userId}/cards")
    public Set<Card> getUserCards(@PathVariable Integer userId) {
        return this.cardService.getUserCards(userId);
    }


    /**
     * export topic
     * @param topicId
     * @param response
     * @param userId
     * @throws Exception
     */
    @GetMapping(value = "/{userId}/export")
    public void exportTopic(@RequestParam Integer topicId, HttpServletResponse response, Integer userId) throws Exception {
        importExportFacade.export(topicId, response);
    }


    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * Search users
     * @return Set<User>
     */
    @RequestMapping(method = RequestMethod.POST, value = "/search")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<User> search(@RequestBody SearchDto searchDto) {
        return searchFacade.searchUser(searchDto);
    }

    /**
     * Create user
     *
     * @param user
     * @return  User
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user, @RequestParam(required = false) String roleName) {
        Set<Role> roles = new HashSet<>();
        ERole enumRole;
        if (roleName != null && !roleName.isEmpty()) {
            try {
                enumRole = ERole.valueOf(roleName);
            } catch (Exception e) {
                throw new NotFoundException("Enum role not found! " + roleName);
            }
            Role role = roleRepository.findByName(enumRole).orElse(null);
            if (role != null) {
                roles.add(role);
            } else {
                roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new NotFoundException("Role user not found!")));
            }
        } else {
            roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new NotFoundException("Role user not found!")));
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        //        this.esearchInsert(user, user.getId().toString()); //ELASTICSEARCH
        return user;
    }


    /**
     * Import file with topics
     *
     * @param file
     * @param userId
     * @return Topic
     */
    @PostMapping(value = "/{userId}/import", consumes = "multipart/form-data")
    public Topic importTopic(@RequestParam("file") MultipartFile file, @PathVariable Integer userId) throws Exception {
        Topic topic = importExportFacade.importFrom(file, userId);
        //topicDao.insert(topic, topic.getId().toString()); //ELASTICSEARCH
        return topic;
    }


    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * update user
     * @param user
     * @param userId
     * @return ResponseEntity<?>
     */
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable Integer userId) {
        User user1 = this.userService.updateUser(userId, user);
//        this.esearchUpdate(user1, userId.toString());//ELASTICSEARCH
        return user1 != null ?
                ResponseEntity.ok(user1) :
                ResponseEntity.status(304).body("Error!");
    }

    /**
     * Update user
     *
     * @param roleName
     * @param userId
     * @return User
     */
    @SuppressWarnings("ALL")
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserRole(@PathVariable Integer userId, @RequestParam(required = false) String roleName) {
        Set<Role> roles = new HashSet<>();
        ERole enumRole;
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found! " + userId));
        user.getRoles().clear();
        if (roleName != null && !roleName.isEmpty()) {
            try {
                enumRole = ERole.valueOf(roleName);
            } catch (Exception e) {
                throw new NotFoundException("Enum role not found! " + roleName);
            }
            Role role = roleRepository.findByName(enumRole).orElse(null);
            if (role != null) {
                roles.add(role);
            } else {
                roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new NotFoundException("Role user not found!")));
            }
        } else {
            roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new NotFoundException("Role user not found!")));
        }
        user.setRoles(roles);

        return this.userRepository.save(user);
    }

    /**
     * Update copy public topic
     *
     * @param userId
     * @param topicId
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public void subscribeOnPublicTopic(@PathVariable Integer userId, @RequestParam Integer topicId) {
        this.userService.addTopic(topicId, userId);
    }

    /**
     * Update copy public topic
     *
     * @param userId
     * @param topicId
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}/unsubscribe")
    @ResponseStatus(HttpStatus.OK)
    public void unsubscribeFromPublicTopic(@PathVariable Integer userId, @RequestParam Integer topicId) {
        this.userService.unsubscribeFromPublicTopic(topicId, userId);
    }

    /**
     * Update copy public topic
     *
     * @param userId
     * @param topicId
     */
    @PutMapping("/{userId}/copy")
    @ResponseStatus(HttpStatus.OK)
    public Topic copyPublicTopic(@PathVariable Integer userId, @RequestParam Integer topicId) {
        Topic topic = this.userService.copyTopic(topicId, userId);
        //topicDao.insert(topic, topic.getId().toString()); //ELASTICSEARCH
        return topic;
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * Delete user
     *
     * @param userId
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Integer userId) {
//        this.esearchDelete(userId.toString()); //ELASTICSEARCH
        this.userService.deleteUser(userId);
    }

/*-----------ELASTICSEARCH--------------*/

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * search user by value in fields
     * @param value
     * @param fields
     * @return Collection<Map<String, Object>>
     */
    @GetMapping("/esearch")
    public Collection<Map<String, Object>> esearch(@RequestParam String value, @RequestParam String[] fields) {
        return userDao.search(value, fields);
    }

    /**
     * find user by id
     * @param userId
     * @return Map<String, Object>
     */
    @GetMapping("/esearch/{userId}")
    public Map<String, Object> esearchById(@PathVariable String userId) {
        return userDao.find(userId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * create user
     * @param user
     * @param userId
     * @return User
     */
    @PostMapping("/esearch/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public User esearchInsert(@RequestBody User user, @PathVariable String userId) {
        return userDao.insert(user, userId); //ELASTICSEARCH
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * update user
     * @param user
     * @param userId
     * @return Map<String, Object>
     */
    @PutMapping("/esearch/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> esearchUpdate(@RequestBody User user, @PathVariable String userId) {
       return userDao.update(userId, user);
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * delete user by id
     * @param userId
     */
    @DeleteMapping("/esearch/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void esearchDelete(@PathVariable String userId) {
        this.userDao.remove(userId);
    }

}