package pe.api.forohub.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pe.api.forohub.domain.answer.ResponseAnswerDTO;
import pe.api.forohub.domain.subject.ResponseSubjectDTO;
import pe.api.forohub.domain.topic.*;
import pe.api.forohub.domain.subject.Subject;
import pe.api.forohub.domain.user.ResponseUserDTO;
import pe.api.forohub.domain.user.User;
import pe.api.forohub.domain.subject.SubjectRepository;
import pe.api.forohub.domain.user.UserRepository;
import pe.api.forohub.exceptions.BadPayloadException;
import pe.api.forohub.exceptions.BadQueryParamValueException;
import pe.api.forohub.services.TopicService;

import java.net.URI;
import java.util.LinkedList;
import java.util.Optional;

@RestController
@RequestMapping("/topics")
@SecurityRequirement(name = "bearer-key")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<ResponseTopicDTO> create(
            @RequestBody @Valid CreateTopicDTO topicDTO,
            UriComponentsBuilder uriComponentsBuilder
    ){
        ResponseTopicDTO response = topicService.createTopic(topicDTO);
        URI uriToTopic = uriComponentsBuilder.path("/topics/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uriToTopic).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTopicDTO> getTopicById(@PathVariable Long id) {
        ResponseTopicDTO response = topicService.getTopicById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseListTopicDTO>> getAll(
            @PageableDefault(size = 5) Pageable pageable,
            @RequestParam(name = "status", required = false) String statusQueryParam
    ) {
        Page<ResponseListTopicDTO> topics = topicService.getAllTopics(pageable, statusQueryParam);
        return ResponseEntity.ok(topics);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseTopicDTO> updateTopic(
            @PathVariable Long id,
            @RequestBody @Valid UpdateTopicDTO topicDTO
    ){
        ResponseTopicDTO response = topicService.updateTopic(id, topicDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id){
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
