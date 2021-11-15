package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.model.User;
import cz.cvut.ear.flashcards.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {


    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void addUserTest_isSuccess() {
        User user = Generator.generateUser();
        user.setId(null);
        User createdUser = userServiceImpl.addUser(user);
        assertNotNull(createdUser);
    }

    @Test
    public void getUserTest_isSuccess() {
        User user = Generator.generateUser();
        user.setId(null);
        user = userServiceImpl.addUser(user);
        assertNotNull(user);
        User get = userServiceImpl.getUser(user.getId());
        assertEquals(user, get);
    }

    @Test(expected = NotFoundException.class)
    public void getUserTest_userDoesNotExists() {
        User get = userServiceImpl.getUser(666);
    }

    @Test
    public void saveTest_isSuccess() {



    }

//    @Test
//    public void getUsersTest() {
//        Set<User> users = new HashSet<>();
//
//        for (int i = 0; i < 10; i++) {
//            User user = Generator.generateUser();
//            user.setId(null);
//            user = userServiceImpl.addUser(user);
//            assertNotNull(user);
//            users.add(user);
//        }
//
//        Set<User> get = userRepository.findAll();
//
//        assertEquals(users, get);
//    }

//    @Test
//    public void updateUserTest() {
//        Role role = Role.USER;
//
//        assertNotNull(role);
//
//        User user = Generator.generateUser();
//        user.setRoles(Collections.singleton(role));
//        user.setId(null);
//
//        user = userServiceImpl.addUser(user);
//
//        assertNotNull(user);
//
//        String u = user.getUsername();
//        String e = user.getEmail();
//
//        user.setEmail("sfadfasfd@newamail.com");
//        user.setUsername("newusername124");
//
//        userServiceImpl.updateUser(user.getId(), user);
//
//        user = userServiceImpl.getUser(user.getId());
//
//        assertNotNull(user);
//
//        assertNotEquals(user.getUsername(), u);
//        assertNotEquals(user.getEmail(), e);
//
//    }

    @Test(expected = NotFoundException.class)
    public void deleteUserTest() {


    }

//    @Test
//    public void addTopicTest() {
//
//        Role role = Role.USER;
//
//        assertNotNull(role);
//
//        User user = Generator.generateUser();
//        user.setRoles(Collections.singleton(role));
//        user.setId(null);
//
//        user = userServiceImpl.addUser(user);
//
//        assertNotNull(user);
//
//        Topic topic = Generator.generateTopic();
//        topic.setId(null);
//        topic.setAuthor("");
//        topic = topicService.addTopic(topic);
//        assertNotNull(topic);
//
//        userServiceImpl.addTopic(user.getId(), topic);
//
//        user = userServiceImpl.getUser(user.getId());
//        topic = topicService.getTopic(topic.getId());
//        assertNotNull(user);
//        assertNotNull(topic);
//
//        assertEquals(user.getTopics().size(), 1);
//        assertEquals(user.getTopics().iterator().next().getAuthor(), user.getUsername());
//        assertEquals(user.getTopics().iterator().next(), topic);
//        assertEquals(topic.getUsers().iterator().next(), user);
//    }

//    @Test
//    public void getTopicsTest() {
//
//
//        Role role = Role.USER;
//        assertNotNull(role);
//
//        User user = Generator.generateUser();
//        user.setRoles(Collections.singleton(role));
//        user.setId(null);
//
//        user = userServiceImpl.addUser(user);
//
//        assertNotNull(user);
//
//        for (int i = 0; i < 10; i++) {
//            Topic topic = Generator.generateTopic();
//            topic.setId(null);
//            topic.setAuthor(null);
//
//            userServiceImpl.addTopic(user.getId(), topic);
//
//        }
//
//        Set<Topic> get = userServiceImpl.getTopics(user.getId());
//
//        assertEquals(get.size(), 10);
//    }
}
