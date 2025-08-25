package fsa.cplorm.seed;

import fsa.cplorm.model.Course;
import fsa.cplorm.model.Role;
import fsa.cplorm.model.User;
import fsa.cplorm.repository.CourseRepository;
import fsa.cplorm.repository.RoleRepository;
import fsa.cplorm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Seeder {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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

    private void seedRolesAndUsers() {
        if (roleRepository.count() > 0)
            return;

        Role adminRole = roleRepository.save(new Role("ADMIN"));
        Role studentRole = roleRepository.save(new Role("STUDENT"));

        if (userRepository.count() > 0)
            return;
        userRepository.save(new User("admin", passwordEncoder.encode("admin"), List.of(adminRole)));
        userRepository.save(new User("student1", passwordEncoder.encode("student1"), List.of(studentRole)));
        userRepository.save(new User("student2", passwordEncoder.encode("student2"), List.of(adminRole, studentRole)));
    }

    @PostConstruct
    public void seed() {
        seedRolesAndUsers();
        seedCourses();
    }
}
