package fsa.cplorm.seed;

import fsa.cplorm.model.Course;
import fsa.cplorm.model.Role;
import fsa.cplorm.model.User;
import fsa.cplorm.repository.CourseRepository;
import fsa.cplorm.repository.RoleRepository;
import fsa.cplorm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class Seeder {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Seeder(CourseRepository courseRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void seedCourses() {
        if (courseRepository.count() > 0)
            return;
        courseRepository.save(new Course("Java"));
        courseRepository.save(new Course("C#"));
        courseRepository.save(new Course("C++"));
        courseRepository.save(new Course("JS"));
    }

    public void seedUsersAndRoles() {
        if (roleRepository.count() > 0)
            return;
        Role adminRole = roleRepository.save(new Role("ADMIN"));
        Role studentRole = roleRepository.save(new Role("STUDENT"));

        if (userRepository.count() > 0)
            return;
        userRepository.save(new User("admin", passwordEncoder.encode("admin"), Set.of(adminRole)));
        userRepository.save(new User("user1", passwordEncoder.encode("user1"), Set.of(studentRole)));
        userRepository.save(new User("user2", passwordEncoder.encode("user2"), Set.of(studentRole)));
    }

    @PostConstruct
    @Transactional
    public void seed() {
        seedCourses();
        seedUsersAndRoles();
    }
}
