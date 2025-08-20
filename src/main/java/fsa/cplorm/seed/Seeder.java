package fsa.cplorm.seed;

import fsa.cplorm.model.Course;
import fsa.cplorm.repository.CourseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Seeder {
    @Autowired
    private CourseRepository courseRepository;

    @PostConstruct
    public void seed() {
        if (courseRepository.count() > 0)
            return;
        courseRepository.save(new Course("Java"));
        courseRepository.save(new Course("C#"));
        courseRepository.save(new Course("C++"));
        courseRepository.save(new Course("JS"));
    }
}
