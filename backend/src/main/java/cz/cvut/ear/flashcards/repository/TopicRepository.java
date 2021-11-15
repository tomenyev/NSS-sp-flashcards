package cz.cvut.ear.flashcards.repository;

import cz.cvut.ear.flashcards.model.Topic;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public interface TopicRepository extends CrudRepository<Topic, Integer> {
    HashSet<Topic> findAll();

    Set<Topic> findAllBySharedIsTrueAndNameContainingIgnoreCase(@NotNull String name);

    Set<Topic> findAllBySharedIsTrueAndTagsContainingIgnoreCase(@NotNull String name);

    Set<Topic> findAllBySharedIsTrueAndAuthorContainingIgnoreCase(@NotNull String name);

    Set<Topic> findAllBySharedIsTrueAndNameContainingIgnoreCaseAndAuthorContainingIgnoreCase(@NotNull String name, @NotNull String author);

    Set<Topic> findAllBySharedIsTrueAndNameContainingIgnoreCaseAndTagsContainingIgnoreCase(@NotNull String name, @NotNull String tags);

    Set<Topic> findAllBySharedIsTrueAndAuthorContainingIgnoreCaseAndTagsContainingIgnoreCase(@NotNull String author, @NotNull String tags);

    Set<Topic> findAllBySharedIsTrueAndAuthorContainingIgnoreCaseAndTagsContainingIgnoreCaseAndNameContainingIgnoreCase(String author, @NotNull String tags, @NotNull String name);

    //-----------

    Set<Topic> findAllByNameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(@NotNull String name, @NotNull String title, @NotNull String description);

    Set<Topic> findAllByTagsContainingIgnoreCase(@NotNull String name);

    Set<Topic> findAllByAuthorContainingIgnoreCase(@NotNull String name);

    Set<Topic> findAllByNameContainingIgnoreCaseAndAuthorContainingIgnoreCase(@NotNull String name, @NotNull String author);

    Set<Topic> findAllByNameContainingIgnoreCaseAndTagsContainingIgnoreCase(@NotNull String name, @NotNull String tags);

    Set<Topic> findAllByAuthorContainingIgnoreCaseAndTagsContainingIgnoreCase(@NotNull String author, @NotNull String tags);

    Set<Topic> findAllByAuthorContainingIgnoreCaseAndTagsContainingIgnoreCaseAndNameContainingIgnoreCase(String author, @NotNull String tags, @NotNull String name);
}
