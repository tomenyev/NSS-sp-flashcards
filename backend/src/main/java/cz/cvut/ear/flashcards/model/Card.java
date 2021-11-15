/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.ear.flashcards.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomen
 */
@Entity
@Table(name = "card")
public class Card extends AbstractEntity implements Serializable {

    @NotNull
    private String question;

    @NotNull
    private String answer;

    @JsonIgnore
    @OrderBy("updatedat DESC")
    @ManyToMany
    private Set<Deck> decks = new HashSet<>();


    public Card() {
    }

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public Card(@NotNull String question, @NotNull String answer, Set<Deck> decks) {
        this.question = question;
        this.answer = answer;
        this.decks = decks;
    }

    public static class Builder {

        private String question;

        private String answer;

        private Set<Deck> decks = new HashSet<>();

        public Builder withQuestion(String question) {
            this.question = question;
            return this;
        }

        public Builder withAnswer(String answer) {
            this.answer = answer;
            return this;
        }

        public Builder withDecks(Set<Deck> decks) {
            this.decks = decks;
            return this;
        }

        public Card build() {
            return new Card(question, answer, decks);
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Set<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Set<Deck> decks) {
        this.decks = decks;
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    @Override
    public String toString() {
        return "Card{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    @Override
    public Card clone() {
        Card card = new Card();
        card.setQuestion(question);
        card.setAnswer(answer);
        return card;
    }
}
