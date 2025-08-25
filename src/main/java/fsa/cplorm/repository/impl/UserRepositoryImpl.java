package fsa.cplorm.repository.impl;

import fsa.cplorm.model.User;
import fsa.cplorm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int count() {
        Session session = entityManager.unwrap(Session.class);
        TypedQuery<Long> query = session.createQuery("select count(*) from User", Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    @Transactional
    public User save(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.persist(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        Session session = entityManager.unwrap(Session.class);
        TypedQuery<User> query = session.createQuery("from User where username = :username", User.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
}
