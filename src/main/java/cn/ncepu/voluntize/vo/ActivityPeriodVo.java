package cn.ncepu.voluntize.vo;

import cn.ncepu.voluntize.entity.ActivityPeriod;
import lombok.Data;

@Data
public class ActivityPeriodVo {
    private String id;
    private String parentStationId;
    private String parentStationName;
    private String parentActivityId;
    private String parentActivityName;
    private String startDate;
    private String endDate;
    private String timePeriod;
    private String requirements;
    private Integer equDuration;
    private Integer amountRequired;
    private Integer amountSigned;

    public ActivityPeriodVo(ActivityPeriod period) {
        this.id = period.getId();
        this.parentStationId = period.getParent().getId();
        this.parentStationName = period.getParent().getName();
        this.parentActivityId = period.getParent().getParentActivity().getId();
        this.parentActivityName = period.getParent().getParentActivity().getName();
        this.startDate = period.getStartDate().toString();
        this.endDate = period.getEndDate().toString();
        this.timePeriod = period.getTimePeriod();
        this.amountRequired = period.getAmountRequired();
        this.equDuration = period.getEquDuration();
        //在此查找该活动的报名记录
        this.amountSigned = period.getRecords().size();
    }

    public ActivityPeriodVo(){

    }
}
