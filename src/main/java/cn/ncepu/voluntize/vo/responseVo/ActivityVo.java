package cn.ncepu.voluntize.vo.responseVo;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

import cn.ncepu.voluntize.vo.ImageVo;
import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.ActivityPeriod;
import cn.ncepu.voluntize.entity.ActivityStation;

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
    private String departmentName;
    private Activity.ActivityStatus status;

    private List<ImageVo> images = new ArrayList<>();
    private List<ActivityStationVo> stations = new ArrayList<>();
    //没有comments，它们需要单独分页返回

    public ActivityVo(Activity activity) {
        this.id = activity.getId();
        this.status = activity.getStatus();
        this.name = activity.getName();
        this.semester = activity.getSemester();
        this.description = activity.getDescription();
        this.departmentId = activity.getDepartment().getId();
        this.departmentName = activity.getDepartment().getName();
        for (Image image : activity.getImages()) images.add(new ImageVo(image));
        for (ActivityStation station : activity.getStations()) stations.add(new ActivityStationVo(station));
    }

    @Data
    static class ActivityStationVo {
        private String id;
        private String name;
        private String linkman;
        private String phoneNum;
        private String description;
        private List<ActivityPeriodVo> periods = new ArrayList<>();

        public ActivityStationVo(ActivityStation station) {
            this.id = station.getId();
            this.name = station.getName();
            this.linkman = station.getLinkman();
            this.phoneNum = station.getPhoneNum();
            this.description = station.getDescription();
            for (ActivityPeriod period : station.getPeriods()) periods.add(new ActivityPeriodVo(period));
        }
    }
}
