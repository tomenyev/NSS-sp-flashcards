package cz.cvut.ear.flashcards.dto;

import javax.validation.constraints.NotNull;

/** Deck DTO (Data Transfer Object)
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
public class DeckDto {

    @NotNull
    private String deckName;

    @NotNull
    private String topicid;

    private String deckAuthor;

    // getters, setters
    
    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getAuthor() {
        return deckAuthor;
    }

    @Override
    public String toString() {
        return "DeckDto{" +
                "deckName='" + deckName + '\'' +
                ", topicid='" + topicid + '\'' +
                ", author='" + deckAuthor + '\'' +
                '}';
    }

    public void setDeckAuthor(String deckAuthor) {
        this.deckAuthor = deckAuthor;
    }

}
