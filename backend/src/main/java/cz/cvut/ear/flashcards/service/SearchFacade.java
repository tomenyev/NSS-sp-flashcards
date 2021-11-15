package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.dto.SearchDto;
import cz.cvut.ear.flashcards.model.Review;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Search facade to manage all search logic
 */
@Service
@SuppressWarnings("ALL")
public class SearchFacade {

    @Autowired
    private TopicService topicService;

    @Autowired
    private ReviewService reviewService;

    @Autowired UserService userService;


    /**
     * search by search dto "author", "tag", "name"
     * @param searchDto search dto
     * @param shared search in shared topics
     * @return set of topics
     */
    public Set<Topic> search(SearchDto searchDto, Boolean shared) {
        if(searchDto == null || searchDto.getSearch() == null || searchDto.getSearch().isEmpty()) {
            return null;
        }
        Map<String, Set<String>> map = this.topicService.parseSearchReqest(searchDto);
        return this.topicService.search(map, shared);
    }

    /**
     * search by search dto "author" "description"
     * @param searchDto search dto
     * @return set of reviews
     */
    public Set<Review> searchReview(SearchDto searchDto) {
        if(searchDto == null || searchDto.getSearch() == null || searchDto.getSearch().isEmpty()) {
            return null;
        }
        Map<String, Set<String>> map = this.reviewService.parseSearchReqest(searchDto);
        return this.reviewService.search(map);
    }

    /**
     * search by search dto "username" "email"
     * @param searchDto search dto
     * @return set of users
     */
    public Set<User> searchUser(SearchDto searchDto) {
        if(searchDto == null || searchDto.getSearch() == null || searchDto.getSearch().isEmpty()) {
            return null;
        }
        Map<String, Set<String>> map = this.userService.parseSearchReqest(searchDto);
        return this.userService.search(map);
    }

}
