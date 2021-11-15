package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.repository.DeckRepository;
import cz.cvut.ear.flashcards.repository.UserRepository;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@SuppressWarnings("ALL")
@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private TopicService topicService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private UserRepository userRepository;


    /**
     * get all user's decks
     * @param userId user id
     * @return set of decks
     */
    @Transactional(readOnly = true)
    public HashSet<Deck> getUserDecks(Integer userId) {
        HashSet<Deck> decks = new HashSet<>();
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {

            user.getTopics().forEach(d -> {
                decks.addAll(d.getDecks());
            });
            return decks;
        }
        return null;
    }

    /**
     * get all topic's decks
     * @param topicId topic id
     * @return set of decks
     */
    @Transactional(readOnly = true)
    public HashSet<Deck> getAllTopicDecks(Integer topicId) {
        Topic topic = topicService.getTopic(topicId);
        return new HashSet<Deck>(topic.getDecks());
    }

    /**
     * create deck and add to the topic
     * @param topicId topic id
     * @param deck new deck
     * @return created deck
     */
    @Transactional
    public Deck addDeck(Integer topicId, Deck deck) {
        deck = deckRepository.save(deck);
        Topic topic = topicService.getTopic(topicId);
        deck.addTopic(topic);
        return deckRepository.save(deck);
    }

    /**
     * update deck
     * @param topicId topic id
     * @param deckId deck id
     * @param newDeck new deck
     * @return updated deck
     */
    @Transactional
    @CachePut(value = "decksCache", key = "#deckId")
    public Deck updateDeck(Integer topicId, Integer deckId, Deck newDeck) {
        Topic topic = topicService.getTopic(topicId);
        Deck deck = deckService.getDeck(deckId);

        if (!deck.getName().equals(newDeck.getName())) {
            deck.setName(newDeck.getName());
        }

        return deckRepository.save(deck);
    }

    /**
     * get deck by id
     * @param deckId deck id
     * @return deck entity
     */
    @Transactional(readOnly = true)
    @Cacheable(value="decksCache", key = "#deckId", unless = "#result==null")
    public Deck getDeck(Integer deckId) {
        Optional<Deck> deck = deckRepository.findById(deckId);
        if (!deck.isPresent()) {
            throw new NotFoundException("Deck not found! ID: " + deckId);
        }
        return deck.get();
    }

    /**
     * delete deck by id
     * @param deckId deck id
     */
    @Transactional
    @CacheEvict(value="decksCache", key = "#deckId")
    public void deleteDeck(int deckId) {
        deckRepository.deleteById(deckId);
    }

}

