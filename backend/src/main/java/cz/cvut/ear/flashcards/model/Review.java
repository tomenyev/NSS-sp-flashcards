/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.ear.flashcards.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Tomen
 */
@Entity
@Table(name = "review")
public class Review extends AbstractEntity implements Serializable {

    @NotNull
    private String author;

    @NotNull
    private String review;

    @NotNull
    private boolean rate;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private User user;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private Topic topic;

    public Review(@NotNull String author, @NotNull String review, @NotNull boolean rate, User user, Topic topic) {
        this.author = author;
        this.review = review;
        this.rate = rate;
        this.user = user;
        this.topic = topic;
    }

    public Review() {}

    public static class Builder {

        private String author;

        private String review;

        private boolean rate;

        private User user;

        private Topic topic;

        public Builder withAuthor(String str) {
            this.author = str;
            return this;
        }

        public Builder withReview(String str) {
            this.review = str;
            return this;
        }

        public Builder withRate(Boolean rate) {
            this.rate = rate;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withTopic(Topic topic) {
            this.topic = topic;
            return this;
        }

        public Review build() {
            return new Review(author, review, rate, user, topic);
        }

    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public boolean isRate() {
        return rate;
    }

    public void setRate(boolean rate) {
        this.rate = rate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @CreationTimestamp
    private LocalDateTime createdat;

    @UpdateTimestamp
    private LocalDateTime updatedat;


    public LocalDateTime getCreatedat() {
        return createdat;
    }

    public void setCreatedat(LocalDateTime createdat) {
        this.createdat = createdat;
    }

    public LocalDateTime getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(LocalDateTime updatedat) {
        this.updatedat = updatedat;
    }

    @Override
    public String toString() {
        return "Review{" +
                ", author='" + author + '\'' +
                ", review='" + review + '\'' +
                ", rate=" + rate +
                ", user=" + user +
                ", topic=" + topic +
                ", createdat=" + createdat +
                ", updatedat=" + updatedat +
                '}';
    }
}

