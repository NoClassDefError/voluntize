package cn.ncepu.voluntize.requestVo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DepartmentUpdateVo {
    private String phoneNum;
    private String manager;
    private String email;
    private ArrayList<ImageVo> images;
}
