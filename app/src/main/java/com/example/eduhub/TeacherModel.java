package com.example.eduhub;

public class TeacherModel {

    private String name, designation, email, phone;

    public TeacherModel() {}

    public TeacherModel(String name, String designation, String email, String phone) {
        this.name = name;
        this.designation = designation;
        this.email = email;
        this.phone = phone;
    }

    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
