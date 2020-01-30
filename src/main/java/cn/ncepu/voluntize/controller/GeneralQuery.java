package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class GeneralQuery {
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/activity")
    @ResponseBody
    public ActivityVo query(String activityId, String stationId, String periodId) {
        if (activityId != null) {
            return new ActivityVo(activityService.findById(activityId));
        } else if (stationId != null) {
            return new ActivityVo(activityService.findByStation(stationId));
        } else if (periodId != null) {
            return new ActivityVo(activityService.findByPeriod(periodId));
        }
        return null;
    }
}
