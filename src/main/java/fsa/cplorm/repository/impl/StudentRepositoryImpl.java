package fsa.cplorm.repository.impl;

import fsa.cplorm.model.Student;
import fsa.cplorm.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Student> findAll() {
        Session session = entityManager.unwrap(Session.class);
        TypedQuery<Student> query = session.createQuery("from Student", Student.class);
        return query.getResultList();
    }

    @Override
    public Student findById(Integer id) {
        Session session = entityManager.unwrap(Session.class);
        return session.find(Student.class, id);
    }

    @Override
    @Transactional
    public Student save(Student student) {
        Session session = entityManager.unwrap(Session.class);
        session.persist(student);
        return student;
    }

    @Override
    @Transactional
    public Student update(Student student) {
        Session session = entityManager.unwrap(Session.class);
        session.merge(student);
        return student;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        Session session = entityManager.unwrap(Session.class);
        Student student = session.find(Student.class, id);
        session.remove(student);
    }
}
