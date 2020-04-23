package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.*;
import cn.ncepu.voluntize.vo.ImageVo;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StudentVo {
    private String studentNum;
    private String idNum;
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
//        idNum = student.getIdNum();
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

    public StudentVo() {

    }
}
