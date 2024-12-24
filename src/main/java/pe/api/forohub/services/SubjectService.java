package pe.api.forohub.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.api.forohub.domain.subject.CreateSubjectDTO;
import pe.api.forohub.domain.subject.ResponseSubjectDTO;
import pe.api.forohub.domain.subject.Subject;
import pe.api.forohub.domain.subject.SubjectRepository;

import java.net.URI;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public ResponseSubjectDTO createSubject(CreateSubjectDTO subjectDTO) {
        Subject newSubject = new Subject();
        newSubject.setName(subjectDTO.name());
        newSubject.setCategory(subjectDTO.category());
        subjectRepository.save(newSubject);
        return new ResponseSubjectDTO(newSubject);
    }

    public ResponseSubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.getReferenceById(id);
        return new ResponseSubjectDTO(subject);
    }

    public Page<ResponseSubjectDTO> getAllSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable)
                .map(ResponseSubjectDTO::new);
    }
}
