package cn.ncepu.voluntize.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DepartmentUpdateVo {
    private String phoneNum;
    private String manager;
    private ArrayList<ImageVo> images;
}
