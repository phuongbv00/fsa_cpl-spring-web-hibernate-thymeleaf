package fsa.cplorm.repository;

import fsa.cplorm.model.Student;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface StudentRepository {
    List<Student> findAll();

    @PreAuthorize("hasRole('ADMIN')")
    Student findById(Integer id);

    Student save(Student student);

    Student update(Student student);

    void deleteById(Integer id);
}
