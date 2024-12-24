package pe.api.forohub.services;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.api.forohub.domain.subject.ResponseSubjectDTO;
import pe.api.forohub.domain.subject.Subject;
import pe.api.forohub.domain.topic.*;
import pe.api.forohub.domain.user.ResponseUserDTO;
import pe.api.forohub.domain.user.User;
import pe.api.forohub.domain.user.UserRepository;
import pe.api.forohub.domain.subject.SubjectRepository;
import pe.api.forohub.exceptions.BadPayloadException;
import pe.api.forohub.domain.answer.ResponseAnswerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import pe.api.forohub.exceptions.BadQueryParamValueException;

import java.util.LinkedList;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public ResponseTopicDTO createTopic(CreateTopicDTO createTopicDTO) {
        Optional<User> authorOfTopic = userRepository.findById(createTopicDTO.idUser());
        Optional<Subject> subjectOfTopic = subjectRepository.findById(createTopicDTO.isSubject());

        if (authorOfTopic.isEmpty()) {
            throw new BadPayloadException("author id doesn't exists");
        }

        if (subjectOfTopic.isEmpty()) {
            throw new BadPayloadException("subject id doesn't exists");
        }

        User user = authorOfTopic.get();
        Subject subject = subjectOfTopic.get();

        if (topicRepository.existsByTitleAndAuthorAndSubject(createTopicDTO.title(), user, subject)) {
            throw new BadPayloadException("There is already a topic with similar title, user and subject");
        }

        Topic topic = new Topic(createTopicDTO.title(), createTopicDTO.message(), user, subject);
        topicRepository.save(topic);

        return new ResponseTopicDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreatedAt(),
                topic.getStatus(),
                new ResponseUserDTO(user.getId(), user.getName(), user.getEmail()),
                new ResponseSubjectDTO(subject.getId(), subject.getName(), subject.getCategory()),
                new LinkedList<>()
        );
    }

    public ResponseTopicDTO getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        return new ResponseTopicDTO(topic);
    }


    public Page<ResponseListTopicDTO> getAllTopics(Pageable pageable, String statusQueryParam) {
        if (statusQueryParam == null || statusQueryParam.isEmpty()) {
            return topicRepository.findAllByStatusNot(pageable, TopicStatus.DELETED)
                    .map(ResponseListTopicDTO::new);
        }

        try {
            TopicStatus status = TopicStatus.fromString(statusQueryParam);
            if (status == TopicStatus.DELETED) {
                throw new IllegalArgumentException("Status 'DELETED' is not valid for this operation.");
            }
            return topicRepository.findByStatus(pageable, status)
                    .map(ResponseListTopicDTO::new);
        } catch (IllegalArgumentException e) {
            throw new BadQueryParamValueException(
                    String.format("Invalid status parameter: %s. Details: %s", statusQueryParam, e.getMessage()), e
            );
        }
    }

    public ResponseTopicDTO updateTopic(Long id, UpdateTopicDTO updateTopicDTO) {
        Topic topic = topicRepository.getReferenceById(updateTopicDTO.id());
        if (topic.getStatus() == TopicStatus.DELETED) {
            throw new EntityNotFoundException("This topic doesn't exists, it was deleted.");
        }
        if (topic.getStatus() != TopicStatus.PENDING) {
            throw new BadPayloadException("This topic cannot be updated, because current status is not PENDING.");
        }
        topic.setTitle(updateTopicDTO.title());
        topic.setMessage(updateTopicDTO.message());
        topic.setStatus(TopicStatus.fromString(updateTopicDTO.status()));
        User user = topic.getAuthor();
        Subject subject = topic.getSubject();
        return new ResponseTopicDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreatedAt(),
                topic.getStatus(),
                new ResponseUserDTO(user.getId(), user.getName(), user.getEmail()),
                new ResponseSubjectDTO(subject.getId(), subject.getName(), subject.getCategory()),
                topic.getAnswers().stream().map(ResponseAnswerDTO::new).toList()
        );

    }

    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new EntityNotFoundException("Topic not found");
        }
        topicRepository.deleteById(id);
    }
}
