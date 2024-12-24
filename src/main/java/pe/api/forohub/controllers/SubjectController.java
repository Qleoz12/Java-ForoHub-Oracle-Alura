package pe.api.forohub.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pe.api.forohub.domain.subject.CreateSubjectDTO;
import pe.api.forohub.domain.subject.ResponseSubjectDTO;
import pe.api.forohub.domain.subject.Subject;
import pe.api.forohub.domain.subject.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.api.forohub.services.SubjectService;

import java.net.URI;

@RestController
@RequestMapping("/subjects")
@SecurityRequirement(name = "bearer-key")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<ResponseSubjectDTO> create(
            @RequestBody @Valid CreateSubjectDTO subjectDTO,
            UriComponentsBuilder uriComponentsBuilder
    ){
        ResponseSubjectDTO response = subjectService.createSubject(subjectDTO);
        URI uriToSubject = uriComponentsBuilder.path("/subjects/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uriToSubject).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSubjectDTO> getSubjectById(@PathVariable Long id) {
        ResponseSubjectDTO response = subjectService.getSubjectById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseSubjectDTO>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        Page<ResponseSubjectDTO> subjects = subjectService.getAllSubjects(pageable);
        return ResponseEntity.ok(subjects);
    }
}
