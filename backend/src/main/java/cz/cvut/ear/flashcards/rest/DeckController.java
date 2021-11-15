package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.esdao.EsDeckDao;
import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.service.CardService;
import cz.cvut.ear.flashcards.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/api/decks")
@SuppressWarnings("ALL")
public class DeckController {

    @Autowired
    private DeckService deckService;

    @Autowired
    private CardService cardService;

    @Autowired
    private EsDeckDao deckDao;

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * Get single deck
     *
     * @param deckId
     * @return Deck
     */
    @GetMapping("/{deckId}")
    public Deck getDeck(@PathVariable Integer deckId) {
        return this.deckService.getDeck(deckId);
    }

    /**
     * Get single deck's cards
     *
     * @param deckId
     * @return HashSet<Card>
     */
    @GetMapping("/{deckId}/cards")
    public HashSet<Card> getCards(@PathVariable Integer deckId) {
        return this.cardService.getDeckCards(deckId);
    }


    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * Create deck
     *
     * @param deck
     * @return Deck
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Deck addDeck(@RequestBody Deck deck, @RequestParam Integer topicId) {
//        this.esearchInsert(deck, deck.getId().toString()); //ELASTICSEARCH
        return this.deckService.addDeck(topicId, deck);
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * Update deck
     *
     * @param deckId
     * @param deck
     * @return Deck
     */
    @PutMapping("/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public Deck updateDeck(@PathVariable Integer deckId, @RequestBody Deck deck,
                           @RequestParam(required = false) Integer topicId) {
        deck = this.deckService.updateDeck(topicId, deckId, deck);
//        this.esearchUpdate(deck, deckId.toString()); //ELASTICSEARCH
        return deck;
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * Delete deck
     *
     * @param deckId
     */
    @DeleteMapping("/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDeck(@PathVariable Integer deckId) {
//        this.esearchDelete(deckId.toString());//ELASTICSEARCH
        this.deckService.deleteDeck(deckId);
    }

    /*-----------ELASTICSEARCH--------------*/

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * search decks by value if fields
     * @param value
     * @param fields
     * @return Decks
     */
    @GetMapping("/esearch")
    public Collection<Map<String, Object>> search(@RequestParam String value, @RequestParam String[] fields) {
        return deckDao.search(value, fields);
    }

    /**
     * find by id
     * @param deckId
     * @return Deck
     */
    @GetMapping("/esearch/{deckId}")
    public Map<String, Object> esearchById(@PathVariable String deckId) {
        return deckDao.find(deckId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * insert deck
     * @param deck
     * @param deckId
     * @return Deck
     */
    @PostMapping("/esearch/{deckId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Deck esearchInsert(@RequestBody Deck  deck, @PathVariable String deckId) {
        return deckDao.insert(deck, deckId); //ELASTICSEARCH
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * update deck
     * @param deck
     * @param deckId
     * @return deck
     */
    @PutMapping("/esearch/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> esearchUpdate(@RequestBody Deck deck, @PathVariable String deckId) {
        return deckDao.update(deckId, deck);
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * delete deck
     * @param deckId
     */
    @DeleteMapping("/esearch/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public void esearchDelete(@PathVariable String deckId) {
        deckDao.remove(deckId);
    }
}
