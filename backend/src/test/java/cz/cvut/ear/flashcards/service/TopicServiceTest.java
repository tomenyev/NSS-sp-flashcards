package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.model.Topic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class TopicServiceTest {

    @Autowired
    TopicService topicService;

    @Autowired
    UserService userServiceImpl;


    @Autowired
    DeckService deckService;

    @Autowired
    CardService cardService;


//    deleteTopic

    @Test
    public void addTopicTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);
        assertNotNull(topic);
    }

    @Test
    public void getTopicTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);

        assertNotNull(topic);
        topic = topicService.getTopic(topic.getId());
        assertNotNull(topic);
    }

    @Test
    public void updateTopicTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);
        assertNotNull(topic);
        String author = topic.getAuthor();

        topic.setAuthor("newauthor");

        topicService.updateTopic(topic.getId(), topic);

        topic = topicService.getTopic(topic.getId());

        assertNotSame(topic.getAuthor(), author);
        assertEquals(topic.getAuthor(), "newauthor");
    }

    @Test(expected = NotFoundException.class)
    public void deleteTopicTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);
        assertNotNull(topic);

        topicService.deleteTopic(topic.getId());
        topicService.getTopic(topic.getId());
    }

//    @Test
//    public void getAllTopicsTest() {
//        Set<Topic> topics = new HashSet<>();
//        for (int i = 0; i < 10; i++) {
//            Topic topic = Generator.generateTopic();
//            topic.setId(null);
//            topic = topicService.addTopic(topic);
//            topics.add(topic);
//        }
//        Set<Topic> get = topicService.getAllTopics();
//        assertEquals(get, topics);
//    }

//    @Test
//    public void getAllUserTopicsTest() {
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
//        for (int i = 0; i < 10; i++) {
//            Topic topic = Generator.generateTopic();
//            topic.setId(null);
//            topic.setAuthor(null);
//            userServiceImpl.addTopic(user.getId(), topic);
//        }

//        Set<Topic> topics = topicService.getAllTopics();
//        Set<Topic> get = topicService.getAllUserTopics(user.getId());
//
//        assertEquals(topics, get);
//        assertEquals(topics.size(), 10);
//
//    }
}
