/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.repository.TopicRepository;
import cz.cvut.ear.flashcards.repository.UserRepository;
import cz.cvut.ear.flashcards.dto.SearchDto;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Topic service class
 */
@SuppressWarnings("ALL")
@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * get all topics
     * @return set of topics
     */
    @Transactional(readOnly = true)
    public HashSet<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    /**
     * get topic by id
     * @param topicId topic id
     * @return topic entity
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "topicsCache", key = "#topicId", unless = "#result==null")
    public Topic getTopic(Integer topicId) {
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (!topic.isPresent())
            throw new NotFoundException("Topic not found! ID: " + topicId);
        return topic.get();
    }

    /**
     * add topic by id
     * @param topic topic entity
     * @return created topic
     */
    @Transactional
    public Topic addTopic(Topic topic) {
        if (topic.getAuthor() == null) {
            return topicRepository.save(topic);
        } else if (topic.getAuthor().length() != 0) {
            User user = userRepository.findByUsername(topic.getAuthor());
            topic.addUser(user);
        }
        return topicRepository.save(topic);
    }

    /**
     * update topic by id
     * @param topicId topic id
     * @param newTopic new topic
     * @return updated topic
     */
    @Transactional
    @CachePut(key = "#topicId", value="topicsCache")
    public Topic updateTopic(Integer topicId, Topic newTopic) {
        Topic topic = this.getTopic(topicId);

        if (!topic.getName().equals(newTopic.getName()) && newTopic.getName() != null) {
            topic.setName(newTopic.getName());
        }
        if (!topic.getDescription().equals(newTopic.getDescription()) && newTopic.getDescription() != null) {
            topic.setDescription(newTopic.getDescription());
        }
        if (!topic.getTags().equals(newTopic.getTags()) && newTopic.getTags() != null) {
            topic.setTags(newTopic.getTags());
        }
        if (!topic.getTitle().equals(newTopic.getTitle()) && newTopic.getTitle() != null) {
            topic.setTitle(newTopic.getTitle());
        }
        if (topic.getShared() == null || (!topic.getShared().equals(newTopic.getShared()) && newTopic.getShared() != null)) {
            topic.setShared(newTopic.getShared());
        }
        return topicRepository.save(topic);
    }

    /**
     * delete topic by id
     * @param topicId topic id
     */
    @Transactional
	@CacheEvict(value="topicsCache", key = "#topicId")
    public void deleteTopic(Integer topicId) {
        topicRepository.deleteById(topicId);
    }

    /**
     * search topic by parsed search reqeust "authors", "names", "tags"
     * @param map map with keys "authors", "names", "tags"
     * @param all search public topics or all topics
     * @return set of topics
     */
    public Set<Topic> search(Map<String, Set<String>> map, boolean all) {


        Set<String> names = map.get("names");
        Set<String> tags = map.get("tags");
        Set<String> authors = map.get("authors");

        if(!authors.isEmpty() && tags.isEmpty() && names.isEmpty()) {
            return authors
                    .stream()
                    .map(all ? topicRepository::findAllByAuthorContainingIgnoreCase :
                            topicRepository::findAllBySharedIsTrueAndAuthorContainingIgnoreCase)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(authors.isEmpty() && !tags.isEmpty() && names.isEmpty()) {
            return tags
                    .stream()
                    .map(all ? topicRepository::findAllByTagsContainingIgnoreCase :
                            topicRepository::findAllBySharedIsTrueAndTagsContainingIgnoreCase)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(authors.isEmpty() && tags.isEmpty() && !names.isEmpty()) {
            return names
                    .stream()
                    .map(n -> all ? topicRepository.findAllByNameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(n,n,n) :
                            topicRepository.findAllBySharedIsTrueAndNameContainingIgnoreCase(n))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(!authors.isEmpty() && !tags.isEmpty() && names.isEmpty()) {
            return authors
                    .stream()
                    .map( a -> tags
                            .stream()
                            .map(t -> all ? this.topicRepository.findAllByAuthorContainingIgnoreCaseAndTagsContainingIgnoreCase(a,t) :
                                    this.topicRepository.findAllBySharedIsTrueAndAuthorContainingIgnoreCaseAndTagsContainingIgnoreCase(a,t))
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet())
                    )
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(authors.isEmpty() && !tags.isEmpty() && !names.isEmpty()) {
            return tags
                    .stream()
                    .map(t -> names
                            .stream()
                            .map(n -> all ? this.topicRepository.findAllByNameContainingIgnoreCaseAndTagsContainingIgnoreCase(n,t) :
                                    this.topicRepository.findAllBySharedIsTrueAndNameContainingIgnoreCaseAndTagsContainingIgnoreCase(n,t))
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet())
                    )
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(!authors.isEmpty() && tags.isEmpty() && !names.isEmpty()) {
            return authors
                    .stream()
                    .map( a -> names
                            .stream()
                            .map( n -> all ? this.topicRepository.findAllByNameContainingIgnoreCaseAndAuthorContainingIgnoreCase(n,a) :
                                    this.topicRepository.findAllBySharedIsTrueAndNameContainingIgnoreCaseAndAuthorContainingIgnoreCase(n,a))
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet())
                    )
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(!authors.isEmpty() && !tags.isEmpty() && !names.isEmpty()) {
            return authors
                    .stream()
                    .map(a -> tags
                            .stream()
                            .map(t -> names
                                    .stream()
                                    .map(n -> all ?  this.topicRepository.findAllByAuthorContainingIgnoreCaseAndTagsContainingIgnoreCaseAndNameContainingIgnoreCase(a,t,n) :
                                            this.topicRepository.findAllBySharedIsTrueAndAuthorContainingIgnoreCaseAndTagsContainingIgnoreCaseAndNameContainingIgnoreCase(a,t,n))
                                    .flatMap(Set::stream)
                                    .collect(Collectors.toSet())
                            )
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet())
                    )
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    /**
     * parse serach request
     * @param searchDto search dto
     * @return parsed reqeust as map
     */
    public Map<String, Set<String>> parseSearchReqest(SearchDto searchDto) {

        String request = searchDto.getSearch();

        if(request == null || request.isEmpty()) {
            return null;
        }

        StringBuilder name = new StringBuilder("");
        StringBuilder tag = new StringBuilder("");
        StringBuilder author = new StringBuilder("");

        Set<String> names = new HashSet<>();
        Set<String> tags = new HashSet<>();
        Set<String> authors = new HashSet<>();

        char[] array = request.toCharArray();

        for(int i = 0; i < array.length; i++) {
            boolean add = false;
            switch (array[i]) {
                case '@':
                    for(++i; i < array.length; i++) {
                        char c = array[i];
                        if(Character.isLetter(c) || Character.isDigit(c)) {
                            author.append(c);
                            add = true;
                        } else {
                            if(c == ' ') {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        authors.add(author.toString());
                        author = new StringBuilder("");
                    }
                    break;
                case '#':
                    add = false;
                    for(++i; i < array.length; i++) {
                        char c = array[i];
                        if(Character.isLetter(c) || Character.isDigit(c)) {
                            tag.append(c);
                            add = true;
                        } else {
                            if(c == ' ') {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        tags.add(tag.toString());
                        tag = new StringBuilder("");
                    }
                    break;
                default:
                    add = false;
                    for(; i < array.length; i++) {
                        char c = array[i];
                        if(Character.isLetter(c) || Character.isDigit(c)) {
                            name.append(c);
                            add = true;
                        } else {
                            if(c == ' ') {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        names.add(name.toString());
                        name = new StringBuilder("");
                    }
                    break;
                case ' ':
                    continue;
                case '\n':
                    continue;
            }
        }

        Map<String, Set<String>> map = new HashMap<>();

        map.put("names", names);
        map.put("tags", tags);
        map.put("authors", authors);

        return map;
    }

}
