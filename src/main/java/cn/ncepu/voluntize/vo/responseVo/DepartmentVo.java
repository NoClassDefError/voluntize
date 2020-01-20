package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Image;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 本类用于显示部门信息
 */
@Data
public class DepartmentVo {

    private String id;
    private String name;
    private String phoneNum;
    private String email;
    private String manager;
    private List<String> images = new ArrayList<>();

    public DepartmentVo(Department department) {
        id = department.getId();
        name = department.getName();
        phoneNum = department.getPhoneNum();
        email = department.getEmail();
        manager = department.getManager();
        for (Image image : department.getImages()) images.add(image.getId());
    }
}
