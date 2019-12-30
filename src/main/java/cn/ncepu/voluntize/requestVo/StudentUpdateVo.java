package cn.ncepu.voluntize.requestVo;

import lombok.Data;

import java.util.ArrayList;

/**
 * Student修改信息时，为了防止信息被外部仿制，
 * 此处不需要传入学号的信息，直接传入要修改的信息，
 * 学号信息根据session对象自动填入。
 *<p>
 * 身份证号，班级，年级随学校数据库的变化而变化，不能修改。
 */
@Data
public class StudentUpdateVo {
    private String name;
    private String phoneNum;
    private String email;
    private ArrayList<ImageVo> profiles;
}
