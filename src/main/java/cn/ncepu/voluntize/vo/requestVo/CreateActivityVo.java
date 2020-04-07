package cn.ncepu.voluntize.vo.requestVo;

import lombok.Data;

@Data
public class CreateActivityVo {
    private String name;
    private String description;

    private String startDate;
    private String endDate;
    private String timePeriod;
    private Integer equDuration;
    private Integer amountRequired;

    private String requirements;
    private String stationName;
    private String linkman;
    private String phoneNum;

    private String imageUrl;
}
