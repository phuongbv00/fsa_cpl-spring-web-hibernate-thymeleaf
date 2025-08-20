package fsa.cplorm.controller;

import fsa.cplorm.dto.StudentDto;
import fsa.cplorm.model.Student;
import fsa.cplorm.repository.StudentRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {
    private final StudentRepository studentRepository;

    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<StudentDto> getAll() {
        return studentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getById(@PathVariable Integer id) {
        Student student = studentRepository.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(student));
    }

    @PostMapping
    public ResponseEntity<StudentDto> create(@RequestBody StudentPayload payload) {
        Student created = studentRepository.save(toEntity(payload));
        StudentDto body = toDto(created);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/students/" + created.getId()));
        return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> update(@PathVariable Integer id, @RequestBody StudentPayload payload) {
        Student existing = studentRepository.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        // Ensure path id wins over payload id
        payload.setId(id);
        Student updated = studentRepository.update(toEntity(payload));
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Student existing = studentRepository.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Student toEntity(StudentPayload dto) {
        Objects.requireNonNull(dto, "payload must not be null");
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        try {
            if (dto.getYob() != null && !dto.getYob().isBlank()) {
                LocalDate localDate = LocalDate.parse(dto.getYob());
                student.setYob(localDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
            } else {
                student.setYob(null);
            }
        } catch (Exception e) {
            student.setYob(null);
        }
        return student;
    }

    private StudentDto toDto(Student student) {
        String yob = null;
        if (student.getYob() != null) {
            yob = LocalDate.ofInstant(student.getYob(), java.time.ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return new StudentDto(student.getId(), student.getName(), yob, student.getEmail(), student.getPhone());
    }

    public static class StudentPayload {
        private Integer id;
        private String name;
        private String yob; // ISO_LOCAL_DATE
        private String email;
        private String phone;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getYob() {
            return yob;
        }

        public void setYob(String yob) {
            this.yob = yob;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
