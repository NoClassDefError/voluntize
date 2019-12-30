package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.responseVo.HttpResult;
import cn.ncepu.voluntize.service.ActivityService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
    public String findIndexActivities() {
        List<Activity> activities = activityService.findStatus(Activity.ActivityStatus.SEND);
        activities.addAll(activityService.findStatus(Activity.ActivityStatus.STARTED));
        activities.addAll(activityService.findStatus(Activity.ActivityStatus.FINISHED));
        return JSON.toJSONString(activities);
    }

    @RequestMapping(value = "/saveActivity", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveActivity(Activity activity) {
        if (session.getAttribute("UserCategory").equals("Admin")) {
            activityService.createOrUpdate(activity);
            return new HttpResult("saveActivity:success");
        }
        return new HttpResult("deleteActivity:no authority");
    }

    @RequestMapping(value = "/deleteActivity", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult deleteActivity(String id) {
        if (session.getAttribute("UserCategory").equals("Admin") &&
                session.getAttribute("UserCategory").equals("Department")) {
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
    public String findAllActivities() {
        if (session.getAttribute("UserCategory").equals("Admin")) {
            List<Activity> activities = activityService.findAll();
            return JSON.toJSONString(activities);
        }
        return null;
    }

    @RequestMapping(value = "/findConfirmingAc", method = RequestMethod.POST)
    @ResponseBody
    public String findConfirmingActivities() {
        if (session.getAttribute("UserCategory").equals("Admin") &&
                session.getAttribute("UserCategory").equals("Department")) {
            List<Activity> activities = activityService.findStatus(Activity.ActivityStatus.CONFIRMING);
            return JSON.toJSONString(activities);
        }
        return null;
    }
}
