package cz.cvut.ear.flashcards.model;

import java.io.Serializable;

public class ReviewPK implements Serializable {
    private Integer id;

    private String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
