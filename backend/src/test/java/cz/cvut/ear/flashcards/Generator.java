package cz.cvut.ear.flashcards;

import cz.cvut.ear.flashcards.model.*;
import cz.cvut.nss.flashcards.model.*;
import cz.cvut.ear.flashcards.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Generator {

    private final static Random RAND = new Random();

    public static int randomInt() {
        return Math.abs(RAND.nextInt() % 1000);
    }

    public static boolean randomBool() {
        return RAND.nextBoolean();
    }

    @Autowired
    private RoleRepository roleRepository;

    public static User generateUser() {
        User user = new User();
        user.setEmail("Email" + randomInt() + "@test.com");
        user.setPassword(String.valueOf(randomInt()));
        user.setUsername("user" + randomInt());
        Set<Role> roles = new HashSet<>();
        user.setId(randomInt());

        return user;
    }

    public static Card generateCard() {
        Card card = new Card();
        card.setAnswer("answ" + randomInt());
        card.setQuestion("question" + randomInt());
        card.setId(randomInt());
        return card;

    }

    public static Deck generateDeck() {
        Deck deck = new Deck();
        deck.setName("name" + randomInt());
        deck.setAuthor("author" + randomInt());
        deck.setId(randomInt());
        return deck;
    }

    public static Review generateReview() {
        Review review = new Review();
        review.setAuthor("test" + randomInt());
        review.setReview("review" + randomInt());
        review.setRate(randomBool());
        review.setId(randomInt());

        return review;
    }

    public static Topic generateTopic() {
        Topic topic = new Topic();
        topic.setName("name" + randomInt());
        topic.setDescription("desc" + randomInt());
        topic.setTags("tags" + randomInt());
        topic.setTitle("title" + randomInt());
        topic.setAuthor("author" + randomInt());
        topic.setId(randomInt());

        return topic;
    }

    public static String generateEmail() {
        return "Email" + randomInt() + "@test.com";
    }
}
