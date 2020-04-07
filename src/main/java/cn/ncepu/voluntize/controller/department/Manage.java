package cn.ncepu.voluntize.controller.department;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.requestVo.CreateActivityVo;
import cn.ncepu.voluntize.vo.requestVo.EvaluateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/department/service")
public class Manage extends BaseController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipateService participateService;

    @RequestMapping("/createActivity")
    @ResponseBody
    public HttpResult createActivity(@RequestBody CreateActivityVo activityVo) {
        return new HttpResult("release:" + activityService.create2(activityVo) );
    }

    @RequestMapping("/saveActivity")
    @ResponseBody
    public HttpResult update(@RequestBody ActivityVo activityVo) {
         return new HttpResult("release:" + activityService.update(activityVo) );
    }

    @RequestMapping("/saveStation")
    @ResponseBody
    public HttpResult updateStation(@RequestBody ActivityStationVo activityStationVo) {
        return new HttpResult("release:" + activityService.updateStation(activityStationVo));
    }

    @RequestMapping("/savePeriod")
    @ResponseBody
    public HttpResult updatePeriod(@RequestBody ActivityPeriodVo activityPeriodVo) {
        return new HttpResult("release:" + activityService.updatePeriod(activityPeriodVo));
    }

    @RequestMapping(value = "/cancel")
    public void cancel(String activityId, String stationId, String periodId) {
        System.out.println(activityId);
        if (activityId != null)
            activityService.deleteActivity(activityId);
        if (stationId != null)
            activityService.deleteActivityStation(stationId);
        if (periodId != null)
            activityService.deleteActivityPeriod(periodId);
    }

    /**
     * 录用
     */
    @RequestMapping("/approve")
    public HttpResult approve(@RequestBody List<String> recordId) {
        logger.info("approved "+recordId);
        return new HttpResult("approve:" + participateService.accept(recordId));
    }

    @RequestMapping("/deny")
    @ResponseBody
    public HttpResult deny(String record) {
        return new HttpResult("deny:" + participateService.deny(record));
    }

    /**
     * 结束录用，开始活动
     */
    @RequestMapping("/start")
    public HttpResult startActivity(String activityId) {
        return new HttpResult("startActivity:" + activityService.startActivity(activityId));
    }

    @RequestMapping("/evaluate")
    public void evaluate(@RequestBody List<EvaluateVo> evaluateVos) {
        participateService.evaluate(evaluateVos);
    }
}
