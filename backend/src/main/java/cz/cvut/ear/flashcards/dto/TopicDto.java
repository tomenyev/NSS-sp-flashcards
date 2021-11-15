package cz.cvut.ear.flashcards.dto;

import javax.validation.constraints.NotNull;

/** Topic DTO (Data Transfer Object)
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
public class TopicDto {
    @NotNull
    private String topicName;
    @NotNull
    private String topicTitle;
    @NotNull
    private String topicDescription;
    @NotNull
    private String topicTags;
    @NotNull
    private String topicAuthor;

    // getters, setters
    
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getTopicTags() {
        return topicTags;
    }

    public void setTopicTags(String topicTags) {
        this.topicTags = topicTags;
    }

    public String getTopicAuthor() {
        return topicAuthor;
    }

    public void setTopicAuthor(String topicAuthor) {
        this.topicAuthor = topicAuthor;
    }

    @Override
    public String toString() {
        return "TopicDto{" +
                "topicName='" + topicName + '\'' +
                ", topicTitle='" + topicTitle + '\'' +
                ", topicDescription='" + topicDescription + '\'' +
                ", topicTags='" + topicTags + '\'' +
                ", topicAuthor='" + topicAuthor + '\'' +
                '}';
    }
}
