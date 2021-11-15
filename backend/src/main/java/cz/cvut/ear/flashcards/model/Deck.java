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
@Table(name = "deck")
public class Deck extends AbstractEntity implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String author;


    @JsonIgnore
    @OrderBy("updatedat DESC")
    @ManyToMany
    private Set<Topic> topics = new HashSet<>();

    @JsonIgnore
    @OrderBy("updatedat DESC")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "card_decks",
            joinColumns = @JoinColumn(
                    name = "decks_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "card_id", referencedColumnName = "id"))
    private Set<Card> cards = new HashSet<>();

    public Deck(@NotNull String name, @NotNull String author, Set<Topic> topics, Set<Card> cards) {
        this.name = name;
        this.author = author;
        this.topics = topics;
        this.cards = cards;
    }

    public Deck() {
    }

    public static class Builder {

        private String name;

        private String author;

        private Set<Topic> topics = new HashSet<>();

        private Set<Card> cards = new HashSet<>();

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withTopics(Set<Topic> topics) {
            this.topics = topics;
            return this;
        }

        public Builder withCards(Set<Card> cards) {
            this.cards = cards;
            return this;
        }

        public Deck build() {
            return new Deck(name, author, topics, cards);
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    @Override
    public Deck clone() {
        Deck deck = new Deck();
        deck.setAuthor("");
        deck.setName(name);
        return deck;
    }
}
