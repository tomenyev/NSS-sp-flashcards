package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.repository.CardRepository;
import cz.cvut.ear.flashcards.repository.UserRepository;
import cz.cvut.ear.flashcards.dto.CardDto;
import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@SuppressWarnings("ALL")
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckService deckService;

    @Autowired
    private UserRepository userRepository;

    /**
     * get all deck's cards by deck id
     * @param deckId deck id
     * @return set of cards
     */
    @Transactional(readOnly = true)
    public HashSet<Card> getDeckCards(Integer deckId) {
        return cardRepository.findAllByDeckId(deckId);
    }

    /**
     * get all user's cards by user id
     * @param userId user id
     * @return set of cards
     */
    @Transactional(readOnly = true)
    public HashSet<Card> getUserCards(Integer userId) {
        HashSet<Card> cards = new HashSet<>();
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.getTopics().forEach(t -> {
                deckService.getAllTopicDecks(t.getId()).forEach(
                        d -> {
                            cards.addAll(this.getDeckCards(d.getId()));
                        }
                );
            });
            return cards;
        }

        return null;
    }

    /**
     * get all topic's cards by topic id
     * @param topicId topic id
     * @return set of cards
     */
    @Transactional(readOnly = true)
    public HashSet<Card> getTopicCards(Integer topicId) {
        HashSet<Card> cards = new HashSet<>();

        deckService.getAllTopicDecks(topicId).forEach(c -> cards.addAll(this.getDeckCards(c.getId())));

        return cards;
    }

    /**
     * get card by id
     * @param cardId card id
     * @return card entity
     */
    @Transactional(readOnly = true)
    @Cacheable(value="cardsCache", key = "#cardId", unless = "#result==null")
    public Card getCard(Integer cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        if (!card.isPresent()) {
            throw new NotFoundException("Card not found! ID: " + cardId);
        }
        return card.get();
    }

    /**
     * create card and add to the deck
     * @param card new card
     * @param deckId deck id
     * @return created card
     */
    @Transactional
    public Card addCard(Card card, Integer deckId) {
        Deck deck = deckService.getDeck(deckId);
        card.addDeck(deck);
        return cardRepository.save(card);
    }

    @Transactional
    @CachePut(value="cardsCache", key = "#cardId")
    public Card updateCard(Integer deckId, Integer cardId, Card newCard) {
        Card card = getCard(cardId);

        if (!card.getAnswer().equals(newCard.getAnswer()) && newCard.getAnswer() != null) {
            card.setAnswer(newCard.getAnswer());
        }

        if (!card.getQuestion().equals(newCard.getQuestion()) && newCard.getQuestion() != null) {
            card.setQuestion(newCard.getQuestion());
        }

        return cardRepository.save(card);
    }

    /**
     * delete card by id
     * @param cardId card id
     */
    @Transactional
    @CacheEvict(value="cardsCache", key = "#cardId")
    public void deleteCard(int cardId) {
        cardRepository.deleteById(cardId);
    }

    /**
     * create card by card dto
     * @param cardDto
     */
    @Transactional
    public void addCardByCardDto(CardDto cardDto) {
        Deck deck = deckService.getDeck(cardDto.getDeckid());
        Card card = new Card.Builder()
                .withQuestion(cardDto.getQuestion())
                .withAnswer(cardDto.getAnswer())
                .withDecks(Collections.singleton(deck))
                .build();
        cardRepository.save(card);
    }
}

