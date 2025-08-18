package fsa.cplorm.controller;

import fsa.cplorm.dto.StudentDto;
import fsa.cplorm.model.Student;
import fsa.cplorm.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
