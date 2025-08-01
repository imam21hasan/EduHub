package com.example.eduhub;

public class StudentModel {
    private String name,blood,session,dept,phone,role;

    public StudentModel() {

    }

    public StudentModel(String name, String blood, String dept, String session, String phone, String role) {
        this.name = name;
        this.blood = blood;
        this.phone = phone;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getBlood() {
        return blood;
    }
    public String getSession() {
        return session;
    }
    public String getDept() {
        return dept;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

}
