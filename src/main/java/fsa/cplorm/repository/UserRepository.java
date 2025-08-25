package fsa.cplorm.repository;

import fsa.cplorm.model.User;

import java.util.Optional;

public interface UserRepository {
    int count();
    User save(User user);
    User findByUsername(String username);
}
