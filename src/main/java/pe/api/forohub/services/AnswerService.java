package pe.api.forohub.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pe.api.forohub.domain.answer.Answer;
import pe.api.forohub.domain.answer.AnswerRepository;
import pe.api.forohub.domain.answer.CreateAnswerDTO;
import pe.api.forohub.domain.answer.ResponseAnswerDTO;
import pe.api.forohub.domain.topic.Topic;
import pe.api.forohub.domain.topic.TopicRepository;
import pe.api.forohub.domain.topic.TopicStatus;
import pe.api.forohub.domain.user.User;
import pe.api.forohub.domain.user.UserRepository;
import pe.api.forohub.exceptions.BadPayloadException;

import java.net.URI;
import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Transactional
    public ResponseAnswerDTO createAnswer(CreateAnswerDTO answerDTO, UriComponentsBuilder uriComponentsBuilder) {
        Optional<User> authorOfAnswer = userRepository.findById(answerDTO.idUser());
        Optional<Topic> topicOfAnswer = topicRepository.findByIdAndStatus(answerDTO.idTopic(), TopicStatus.PENDING);

        if (authorOfAnswer.isEmpty() || topicOfAnswer.isEmpty()) {
            throw new BadPayloadException("user id doesn't exists or topic id is not in status pending or doesn't exists");
        }

        User author = authorOfAnswer.get();
        Topic topic = topicOfAnswer.get();
        Answer newAnswer = new Answer(answerDTO.message(), answerDTO.solution(), topic, author);
        answerRepository.save(newAnswer);

        URI uriToAnswer = uriComponentsBuilder.path("/answers/{id}").buildAndExpand(newAnswer.getId()).toUri();
        return new ResponseAnswerDTO(newAnswer);
    }

    public ResponseAnswerDTO getAnswerById(Long id) {
        Answer answer = answerRepository.getReferenceById(id);
        return new ResponseAnswerDTO(answer);
    }
}
