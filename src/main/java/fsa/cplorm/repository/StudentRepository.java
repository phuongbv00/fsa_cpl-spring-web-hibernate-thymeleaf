package fsa.cplorm.repository;

import fsa.cplorm.model.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> findAll();
    Student findById(Integer id);
    Student save(Student student);
    Student update(Student student);
    void deleteById(Integer id);
}
