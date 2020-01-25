package cn.ncepu.voluntize.controller.department;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.requestVo.EvaluateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController("/department/service")
public class Manage extends BaseController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipateService participateService;

    /**
     * 测试信息
     * ·    *
     */
    @RequestMapping("/saveActivity")
    @ResponseBody
    public HttpResult releaseOrUpdate(@RequestBody ActivityVo activityVo) {
        AtomicReference<String> result = new AtomicReference<>(activityService.createOrUpdate(activityVo));
        return new HttpResult("release activity:" + result);
    }

    @RequestMapping("/saveStation")
    @ResponseBody
    public HttpResult saveStation(@RequestBody ActivityStationVo activityStationVo) {
        return new HttpResult("release activity station:" + activityService.updateStation(activityStationVo));
    }

    @RequestMapping("/savePeriod")
    @ResponseBody
    public HttpResult savePeriod(ActivityPeriodVo activityPeriodVo) {
        return new HttpResult("release activity period:" + activityService.updatePeriod(activityPeriodVo));
    }

    @RequestMapping("/cancel")
    public void cancel(String activityId, String stationId, String periodId) {
        if (activityId != null)
            activityService.deleteActivity(activityId);
        if (stationId != null)
            activityService.deleteActivityStation(stationId);
        if (periodId != null)
            activityService.deleteActivityPeriod(periodId);
    }

    @RequestMapping("/approve")
    public void approve(@RequestBody List<String> recordId) {
        participateService.accept(recordId);
    }

    @RequestMapping("/evaluate")
    public void evaluate(@RequestBody List<EvaluateVo> evaluateVos) {
        participateService.evaluate(evaluateVos);
    }
}
