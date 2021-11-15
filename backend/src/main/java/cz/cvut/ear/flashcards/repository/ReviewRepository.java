package cz.cvut.ear.flashcards.repository;

import cz.cvut.ear.flashcards.model.Review;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    HashSet<Review> findAll();

    Optional<Review> findById(Integer id);

    HashSet<Review> findAllByTopicId(Integer id);

    HashSet<Review> findAllByUserId(Integer id);

    Integer countByRateIsTrueAndTopic_Id(Integer topic_id);

    Integer countByRateIsFalseAndTopic_Id(Integer topic_id);

    HashSet<Review> findAllByTopic_id(Integer topic_id);

    Set<Review> findAllByAuthorContainingIgnoreCase(@NotNull String author);

    Set<Review> findAllByReviewContainingIgnoreCase(@NotNull String author);

    Set<Review> findAllByReviewContainingIgnoreCaseAndAuthorContainingIgnoreCase(@NotNull String review, @NotNull String author);

}
