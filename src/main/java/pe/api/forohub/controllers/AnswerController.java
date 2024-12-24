package pe.api.forohub.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import pe.api.forohub.services.AnswerService;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ResponseAnswerDTO> create(
            @RequestBody @Valid CreateAnswerDTO answerDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        ResponseAnswerDTO responseAnswerDTO = answerService.createAnswer(answerDTO, uriComponentsBuilder);
        URI uriToAnswer = uriComponentsBuilder.path("/answers/{id}").buildAndExpand(responseAnswerDTO.idAnswer()).toUri();
        return ResponseEntity.created(uriToAnswer).body(responseAnswerDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAnswerDTO> getById(@PathVariable Long id) {
        ResponseAnswerDTO responseAnswerDTO = answerService.getAnswerById(id);
        return ResponseEntity.ok(responseAnswerDTO);
    }
}