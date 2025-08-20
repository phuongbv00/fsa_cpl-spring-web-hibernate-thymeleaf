package fsa.cplorm.controller;

import fsa.cplorm.dto.StudentDto;
import fsa.cplorm.model.Course;
import fsa.cplorm.model.Enrollment;
import fsa.cplorm.model.Student;
import fsa.cplorm.repository.CourseRepository;
import fsa.cplorm.repository.EnrollmentRepository;
import fsa.cplorm.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentController(StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("")
    public String index(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "student/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "student/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute StudentDto student) {
        studentRepository.save(toEntity(student));
        return "redirect:/student";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        Student student = studentRepository.findById(id);
        model.addAttribute("student", toDto(student));
        return "student/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute StudentDto student) {
        studentRepository.update(toEntity(student));
        return "redirect:/student";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return "redirect:/student";
    }

    @GetMapping("{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Student student = studentRepository.findById(id);
        model.addAttribute("student", toDto(student));
        List<Enrollment> enrollments = student.getEnrollments();
        model.addAttribute("enrollments", enrollments);
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "student/detail";
    }

    @GetMapping("{id}/enroll/{courseId}")
    @Transactional
    public String enroll(@PathVariable Integer id, @PathVariable Long courseId) {
        Student student = studentRepository.findById(id);
        Course course = courseRepository.findById(courseId).orElseThrow();
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        student.setEnrollmentCount(student.getEnrollmentCount() + 1);
        enrollmentRepository.save(enrollment);
        return "redirect:/student/" + id;
    }

    private Student toEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setPhone(studentDto.getPhone());
        try {
            LocalDate localDate = LocalDate.parse(studentDto.getYob());
            student.setYob(localDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
        } catch (Exception e) {
            student.setYob(null);
        }
        return student;
    }

    private StudentDto toDto(Student student) {
        String yob = null;
        if (student.getYob() != null) {
            yob = LocalDate.ofInstant(student.getYob(), java.time.ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return new StudentDto(student.getId(), student.getName(), yob, student.getEmail(), student.getPhone());
    }
}
