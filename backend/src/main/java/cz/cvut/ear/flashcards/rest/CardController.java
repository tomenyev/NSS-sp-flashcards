package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.esdao.EsCardDao;
import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/cards")
@SuppressWarnings("ALL")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private EsCardDao cardDao;

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * Get card by id
     *
     * @param cardId
     * @return Card
     */
    @GetMapping("/{cardId}")
    public Card getCard(@PathVariable int cardId) {
        return this.cardService.getCard(cardId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * Create card and put it to card with cardId
     *
     * @param card
     * @return Card
     */
    @PostMapping("/")
    public Card addCard(@RequestBody Card card, @RequestParam Integer deckId) {
        card = cardService.addCard(card, deckId);
//        this.esearchInsert(card, card.getId().toString());// ELASTICSEARCH
        return card;
    }


    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * Update card
     *
     * @param cardId
     * @param card
     * @param deckId
     * @return Card
     */
    @PutMapping("/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    public Card updateCard(@PathVariable Integer cardId, @RequestBody Card card, @RequestParam Integer deckId) {
        card = cardService.updateCard(deckId, cardId, card);
//        this.esearchUpdate(card,card.getId().toString());// ELASTICSEARCH
        return card;
    }


    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * Delete card
     * @param cardId
     */
    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCard(@PathVariable Integer cardId) {
//        this.esearchDelete(cardId.toString()); // ELASTICSEARCH
        cardService.deleteCard(cardId);
    }

    /*-----------ELASTICSEARCH--------------*/

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * find all cards with value in fields
     * @param value
     * @param fields
     * @return collection of Cards
     */
    @GetMapping("/esearch")
    public Collection<Map<String, Object>> search(@RequestParam String value, @RequestParam String[] fields) {
        return cardDao.search(value, fields);
    }

    /**
     * find card by id
     * @param cardId
     * @return Card
     */
    @GetMapping("/esearch/{cardId}")
    public Map<String, Object> esearchById(@PathVariable String cardId) {
        return cardDao.find(cardId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * insert card
     * @param card
     * @param cardId
     * @return Card
     */
    @PostMapping("/esearch/{cardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Card esearchInsert(@RequestBody Card  card, @PathVariable String cardId) {
        return cardDao.insert(card, cardId); //ELASTICSEARCH
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * update card
     * @param card
     * @param cardId
     * @return Card
     */
    @PutMapping("/esearch/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> esearchUpdate(@RequestBody Card card, @PathVariable String cardId) {
        return cardDao.update(cardId, card);
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * delet card
     * @param cardId
     */
    @DeleteMapping("/esearch/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    public void esearchDelete(@PathVariable String cardId) {
        cardDao.remove(cardId);
    }
}


