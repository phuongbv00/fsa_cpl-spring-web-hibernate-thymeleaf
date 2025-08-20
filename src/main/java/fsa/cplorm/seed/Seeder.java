package fsa.cplorm.seed;

import fsa.cplorm.model.Course;
import fsa.cplorm.model.User;
import fsa.cplorm.repository.CourseRepository;
import fsa.cplorm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Seeder {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void seedCourses() {
        if (courseRepository.count() > 0)
            return;
        courseRepository.save(new Course("Java"));
        courseRepository.save(new Course("C#"));
        courseRepository.save(new Course("C++"));
        courseRepository.save(new Course("JS"));
    }

    public void seedUsers() {
        if (userRepository.count() > 0)
            return;
        userRepository.save(new User("admin", passwordEncoder.encode("admin")));
        userRepository.save(new User("user1", passwordEncoder.encode("user1")));
        userRepository.save(new User("user2", passwordEncoder.encode("user2")));
    }

    @PostConstruct
    @Transactional
    public void seed() {
        seedCourses();
        seedUsers();
    }
}
