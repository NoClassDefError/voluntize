package cn.ncepu.voluntize.vo.requestVo;

import lombok.Data;

@Data
public class UserUpdateVoAdmin {
    private String id;
    private Integer category;
    private String name;
    private String classs;
    private String grade;
    private String major;
    private String gender;
    private String idNum;
    private String manager;
    private String password;//若不提供，则初始密码为123456
    private String school;
}