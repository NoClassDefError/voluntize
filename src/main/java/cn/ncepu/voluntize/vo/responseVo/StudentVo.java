package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.*;
import cn.ncepu.voluntize.vo.ImageVo;
import lombok.Data;

import java.util.ArrayList;

public class StudentVo {
    private String studentNum;
    private String idNum;

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public ArrayList<ImageVo> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<ImageVo> profiles) {
        this.profiles = profiles;
    }

    private String gender;
    private String name;
    private String major;
    private String grade;
    private String classs;
    private String phoneNum;
    private String email;
    private String school;
    private int totalDuration;

    private ArrayList<ImageVo> profiles = new ArrayList<>();
    //不需要查找comments，comments只需从activity下找
    //不绑定records，records要单独分页返回
    public StudentVo(Student student) {
        studentNum = student.getStudentNum();
        idNum = student.getIdNum();
        name = student.getName();
        major = student.getMajor();
        grade = student.getGrade();
        classs = student.getClasss();
        phoneNum = student.getPhoneNum();
        email = student.getEmail();
        school = student.getSchool();
        gender = student.getGender();
        totalDuration = student.getTotalDuration();
        for (Image image : student.getProfiles()) profiles.add(new ImageVo(image));

    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }
}
