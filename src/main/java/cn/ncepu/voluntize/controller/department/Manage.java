package cn.ncepu.voluntize.controller.department;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.requestVo.EvaluateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
/**
 * TODO 权限验证
 */
@RestController("/manage")
public class Manage extends BaseController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipateService participateService;

    @RequestMapping("/release")
    @ResponseBody
    public HttpResult releaseOrUpdate(@RequestBody ActivityVo activityVo) {
        return new HttpResult("release activity:", activityService.createOrUpdate(activityVo));
    }

    @RequestMapping("/cancel")
    public void cancel(String activityId) {
        activityService.deleteActivity(activityId);
    }

    @RequestMapping("/approve")
    public void approve(@RequestBody ArrayList<String> recordId) {
        participateService.accept(recordId);
    }

    @RequestMapping("/evaluate")
    public void evaluate(@RequestBody ArrayList<EvaluateVo> evaluateVos) {
        participateService.evaluate(evaluateVos);
    }
}
