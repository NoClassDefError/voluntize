package cn.ncepu.voluntize.vo.responseVo;

import lombok.Data;

@Data
public class DepartmentExcelVo {
    private String id;
    private String name;
    private Double aveStar;

    public DepartmentExcelVo(String id, String name, Double aveStar) {
        this.id = id;
        this.name = name;
        this.aveStar = aveStar;
    }
}
