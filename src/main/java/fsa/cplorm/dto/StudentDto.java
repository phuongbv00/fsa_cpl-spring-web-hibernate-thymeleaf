package fsa.cplorm.dto;

import java.io.Serializable;

/**
 * DTO for {@link fsa.cplorm.model.Student}
 */
public class StudentDto implements Serializable {
    private final Integer id;
    private final String name;
    private final String yob;
    private final String email;
    private final String phone;

    public StudentDto(Integer id, String name, String yob, String email, String phone) {
        this.id = id;
        this.name = name;
        this.yob = yob;
        this.email = email;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYob() {
        return yob;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}