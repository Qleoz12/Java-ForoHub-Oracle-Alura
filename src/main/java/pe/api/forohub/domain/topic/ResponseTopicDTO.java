package pe.api.forohub.domain.topic;

import pe.api.forohub.domain.answer.ResponseAnswerDTO;
import pe.api.forohub.domain.subject.ResponseSubjectDTO;
import pe.api.forohub.domain.user.ResponseUserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ResponseTopicDTO(
        Long id,
        String title,
        String message,
        LocalDateTime createdAt,
        TopicStatus status,
        ResponseUserDTO author,
        ResponseSubjectDTO subject,
        List<ResponseAnswerDTO> answers
) {

    public ResponseTopicDTO(Topic topic) {
        this(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreatedAt(),
                topic.getStatus(),
                new ResponseUserDTO(topic.getAuthor()),
                new ResponseSubjectDTO(topic.getSubject()),
                topic.getAnswers().stream()
                        .map(ResponseAnswerDTO::new)
                        .collect(Collectors.toList())
        );
    }
}
