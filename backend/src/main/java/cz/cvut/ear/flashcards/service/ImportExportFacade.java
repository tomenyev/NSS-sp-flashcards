package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * import export facade
 */
@Service
@SuppressWarnings("ALL")
public class ImportExportFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private CardService cardService;

    /**
     * import topic from txt file and add imported topic to user by id
     * @param file txt file
     * @param userId user id
     * @return imported topic
     * @throws Exception
     */
    public Topic importFrom(MultipartFile file, Integer userId) throws Exception {
        User user = userService.getUser(userId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        String str;
        reader.readLine();
        reader.readLine();

        Topic topic = new Topic.Builder()
                .withName(reader.readLine())
                .withTitle(reader.readLine())
                .withDescription(reader.readLine())
                .withTags(reader.readLine())
                .withShared(false)
                .build();
        reader.readLine();

        topic.setAuthor(user.getUsername());

        topic = topicService.addTopic(topic);

        reader.readLine();

        while ((str = reader.readLine()) != null) {
            if (str.equals("no decks")) {
                break;
            }
            if (str.equals("Deck")) {
                reader.readLine();
                str = reader.readLine();
                Deck deck = new Deck();
                deck.setName(str);
                reader.readLine();
                deck.setAuthor(user.getUsername());
                deck = deckService.addDeck(topic.getId(), deck);
                str = reader.readLine();
                if (str.equals("no cards")) {
                    reader.readLine();
                } else {
                    while (!(str = reader.readLine()).equals("};")) {
                        Card card = new Card.Builder()
                                .withAnswer(str)
                                .withQuestion(reader.readLine())
                                .build();
                        cardService.addCard(card, deck.getId());
                        reader.readLine();
                        if (reader.readLine().equals("};")) {
                            break;
                        }
                    }
                }

            }
        }
//        userService.addTopic(userId, topic.getId());
        return topic;
    }

    /**
     * export topic, parse topic to the txt file
     * @param topicId topic id
     * @param response response
     * @throws Exception
     */
    public void export(Integer topicId, HttpServletResponse response) throws Exception {
        //set file name and content type
        String filename = "topic.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        Topic topic = topicService.getTopic(topicId);

        PrintWriter br = new PrintWriter(response.getWriter());

        br.println("Topic");
        br.println("{");
        br.println(topic.getName());
        br.println(topic.getTitle());
        br.println(topic.getDescription());
        br.println(topic.getTags());
        br.println(topic.getAuthor());
        br.println("};");
        if (topic.getDecks() == null || topic.getDecks().size() == 0) {
            br.println("no decks");
            return;
        }

        for (Deck deck : topic.getDecks()) {
            br.println("Deck");
            br.println("{");
            br.println(deck.getName());
            br.println(deck.getAuthor());
            if (deck.getCards() == null || deck.getCards().size() == 0) {
                br.println("no cards");
            } else {
                for (Card card : deck.getCards()) {
                    br.println("[");
                    br.println(card.getQuestion());
                    br.println(card.getAnswer());
                    br.println("];");
                }
            }
            br.println("};");
        }
    }

}
