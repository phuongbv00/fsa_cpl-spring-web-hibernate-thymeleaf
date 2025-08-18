package fsa.cplorm.repository.impl;

import fsa.cplorm.model.Student;
import fsa.cplorm.repository.StudentRepository;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
    private final SessionFactory sessionFactory;

    public StudentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Student> findAll() {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Student> query = session.createQuery("from Student", Student.class);
            return query.getResultList();
        }
    }

    @Override
    public Student findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Student.class, id);
        }
    }

    @Override
    public Student save(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(student);
            session.getTransaction().commit();
            return student;
        }
    }

    @Override
    public Student update(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(student);
            session.getTransaction().commit();
            return student;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Student student = session.find(Student.class, id);
            session.remove(student);
            session.getTransaction().commit();
        }
    }
}
