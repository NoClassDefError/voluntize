package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.ActivityStation;
import lombok.Data;

@Data
public class ActivityResponseVo {
    private String activityId;
    private String stationId;
    private String periodId;
    private String activityName;
    private String stationName;
    private int activityStatus;
    private Integer amountSigned;
    private Integer amountPassed = 0;

    public ActivityResponseVo(Activity activity){
        this.activityId = activity.getId();
        this.activityName = activity.getName();
        this.activityStatus = activity.getStatusId();
        ActivityStation activityStation = activity.getStations().get(0);
        this.stationId = activityStation.getId();
        this.stationName = activityStation.getName();
        this.periodId = activityStation.getPeriods().get(0).getId();
    }
}
