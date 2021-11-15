package cz.cvut.ear.flashcards.dto;

/** Review DTO (Data Transfer Object)
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
public class ReviewDto {
    private String review;
    private Boolean rate;
    private String author;

    
    //getters, setters
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Boolean getRate() {
        return rate;
    }

    public void setRate(Boolean rate) {
        this.rate = rate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
