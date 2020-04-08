package cn.ncepu.voluntize.vo;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.ActivityPeriod;
import cn.ncepu.voluntize.entity.ActivityStation;

import javax.persistence.EntityNotFoundException;

/**
 * 用于显示Activity信息，一般情况下总是与Period和Station合并显示，但也有Period单独显示的情况
 */
@Data
public class ActivityVo {
    private String id;
    private String name;
    private String semester;
    private String description;
    private String departmentId;
    private String departmentName;//前端修改无效
    private int status;

    private List<ImageVo> images = new ArrayList<>();
    private List<ActivityStationVo> stations = new ArrayList<>();
    //没有comments，它们需要单独分页返回

    public ActivityVo(Activity activity) {
        try {
            this.id = activity.getId();
            this.status = activity.getStatusId();
            this.name = activity.getName();
            this.description = activity.getDescription();
            this.departmentId = activity.getDepartment().getId();
            this.departmentName = activity.getDepartment().getName();
//            for (Image image : activity.getImages()) images.add(new ImageVo(image));
            for (ActivityStation station : activity.getStations()) stations.add(new ActivityStationVo(station));
        } catch (NullPointerException e) {
            throw new EntityNotFoundException("No activity found!");
        }
    }

    public ActivityVo() {

    }
}
