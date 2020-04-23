package cn.ncepu.voluntize.vo;

import cn.ncepu.voluntize.entity.ActivityPeriod;
import cn.ncepu.voluntize.entity.ActivityStation;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActivityStationVo {
    private String id;
    private String name;
    private String linkman;
    private String phoneNum;
    private String description;
    private String parentId;
    private List<ActivityPeriodVo> periods = new ArrayList<>();

    public ActivityStationVo(ActivityStation station) {
        this.id = station.getId();
        this.parentId= station.getParentActivity().getId();
        this.name = station.getName();
        this.linkman = station.getLinkman();
        this.phoneNum = station.getPhoneNum();
        this.description = station.getDescription();
        for (ActivityPeriod period : station.getPeriods()) periods.add(new ActivityPeriodVo(period));
    }

    public ActivityStationVo(){}
}
