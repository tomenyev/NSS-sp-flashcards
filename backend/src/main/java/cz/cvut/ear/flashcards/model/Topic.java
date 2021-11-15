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
@Table(name = "topic")
@NamedQuery(name = "Topic.findAllTopics", query = "SELECT e FROM Topic e")
public class Topic extends AbstractEntity implements Serializable {


    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String tags;

    private String author;

    @NotNull
    private String title;

    private Boolean shared;

    @JsonIgnore
    @ManyToMany
    @OrderBy("updatedat DESC")
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("updatedat DESC")
    @JoinTable(
            name = "deck_topics",
            joinColumns = @JoinColumn(
                    name = "topics_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "deck_id", referencedColumnName = "id"))
    private Set<Deck> decks = new HashSet<>();



    public Topic() { }

    public Topic(@NotNull String name, @NotNull String description, @NotNull String tags, String author, @NotNull String title, Boolean shared, Set<User> users, Set<Deck> decks) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.author = author;
        this.title = title;
        this.shared = shared;
        this.users = users;
        this.decks = decks;
    }

    public static class Builder {

        private String name;

        private String description;

        private String tags;

        private String author;

        private String title;

        private Boolean shared;

        private Set<User> users = new HashSet<>();

        private Set<Deck> decks = new HashSet<>();

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withTags(String tags) {
            this.tags = tags;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withShared(Boolean shared) {
            this.shared = shared;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withUsers(Set<User> users) {
            this.users = users;
            return this;
        }

        public Builder withDecks(Set<Deck> decks) {
            this.decks = decks;
            return this;
        }



        public Topic build() {
            return new Topic( name, description, tags, author, title, shared, users, decks);
        }
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Set<Deck> decks) {
        this.decks = decks;
    }

    public void addDeck(Deck deck) {
        this.decks.add(deck);
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean sheared) {
        this.shared = sheared;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    @Override
    public Topic clone() {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setTags(tags);
        topic.setShared(false);
        topic.setDescription(description);
        topic.setName(name);
        topic.setAuthor("");
        return topic;
    }
}
