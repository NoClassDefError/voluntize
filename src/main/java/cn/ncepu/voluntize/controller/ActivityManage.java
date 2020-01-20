package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.vo.responseVo.ActivityVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import cn.ncepu.voluntize.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于管理员与部门发布和管理志愿活动
 */
@RestController
public class ActivityManage extends BaseController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private HttpSession session;

    /**
     * 首页展示的志愿活动，除了审核阶段的志愿活动，其它都可以显示
     *
     * @return JSON数组
     */
    @RequestMapping(value = "/findIndexActivities", method = RequestMethod.POST)
    @ResponseBody
    public List<ActivityVo> findIndexActivities() {
        List<Activity> activities = activityService.findStatus(Activity.ActivityStatus.SEND);
        activities.addAll(activityService.findStatus(Activity.ActivityStatus.STARTED));
        activities.addAll(activityService.findStatus(Activity.ActivityStatus.FINISHED));
        List<ActivityVo> activityVos = new ArrayList<>();
        for (Activity activity : activities) activityVos.add(new ActivityVo(activity));
        return activityVos;
    }

    @RequestMapping(value = "/saveActivity", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveActivity(@RequestBody Activity activity) {
        if ("Admin".equals(session.getAttribute("UserCategory")) ||
                "Department".equals(session.getAttribute("UserCategory"))) {
            activityService.createOrUpdate(activity);
            return new HttpResult("saveActivity:success");
        }
        return new HttpResult("saveActivity:no authority");
    }

    @RequestMapping(value = "/deleteActivity", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult deleteActivity(String id) {
        if ("Admin".equals(session.getAttribute("UserCategory")) ||
                "Department".equals(session.getAttribute("UserCategory"))) {
            activityService.deleteActivity(id);
            return new HttpResult("deleteActivity:success");
        }
        return new HttpResult("deleteActivity:no authority");
    }

    /**
     * 查询之前需要使用session，判断管理员身份
     *
     * @return json
     */
    @RequestMapping(value = "/findAllAc", method = RequestMethod.POST)
    @ResponseBody
    public List<Activity> findAllActivities() {
        if ("Admin".equals(session.getAttribute("UserCategory"))) return activityService.findAll();
        return null;
    }

    @RequestMapping(value = "/findConfirmingAc", method = RequestMethod.POST)
    @ResponseBody
    public List<Activity> findConfirmingActivities() {
        if ("Admin".equals(session.getAttribute("UserCategory")) ||
                "Department".equals(session.getAttribute("UserCategory")))
            return activityService.findStatus(Activity.ActivityStatus.CONFIRMING);
        return null;
    }
}
