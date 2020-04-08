package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Student;
import lombok.Data;

/**
 * 与StudentVo就差一个头像，导出excel不需要头像，就不要查询
 */
@Data
public class StudentExcelVo {
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
    public StudentExcelVo(Student student) {
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
    }
}
