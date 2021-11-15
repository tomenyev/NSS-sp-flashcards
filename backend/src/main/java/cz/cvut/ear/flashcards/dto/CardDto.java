package cz.cvut.ear.flashcards.dto;

/** Card DTO (Data Transfer Object)
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
public class CardDto {

    private String question;

    private String answer;

    private Integer deckid;

    // getters, setters
    
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

    public Integer getDeckid() {
        return deckid;
    }

    public void setDeckid(Integer deckid) {
        this.deckid = deckid;
    }
}
