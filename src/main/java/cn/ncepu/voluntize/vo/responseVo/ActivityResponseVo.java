package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.ActivityStation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActivityResponseVo {

    private String activityId;
    private String stationId;
    private String periodId;
    private String activityName;
    private String stationName;
    private int activityStatus;
    private int amountRequired;
    private String description;

    public ActivityResponseVo(){
    }

    public ActivityResponseVo(Activity activity){
        this.activityId = activity.getId();
        this.description = activity.getDescription();
        this.activityName = activity.getName();
        this.activityStatus = activity.getStatusId();
        ActivityStation activityStation = activity.getStations().get(0);
        this.stationId = activityStation.getId();
        this.stationName = activityStation.getName();
        this.periodId = activityStation.getPeriods().get(0).getId();
        this.amountRequired = activityStation.getPeriods().get(0).getAmountRequired();
    }
}
