package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.vo.ActivityVo;
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



    @RequestMapping(value = "/saveActivity", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveActivity(@RequestBody ActivityVo activity) {
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
    public List<ActivityVo> findAllActivities() {
        if ("Admin".equals(session.getAttribute("UserCategory"))) {
            List<ActivityVo> activityVos = new ArrayList<>();
            for (Activity activity : activityService.findAll())
                activityVos.add(new ActivityVo(activity));
            return activityVos;
        }
        return null;
    }

    @RequestMapping(value = "/findConfirmingAc", method = RequestMethod.POST)
    @ResponseBody
    public List<ActivityVo> findConfirmingActivities() {
        if ("Admin".equals(session.getAttribute("UserCategory")) ||
                "Department".equals(session.getAttribute("UserCategory"))) {
            List<ActivityVo> activityVos = new ArrayList<>();
            for (Activity activity : activityService.findStatus(Activity.ActivityStatus.CONFIRMING))
                activityVos.add(new ActivityVo(activity));
            return activityVos;
        }
        return null;
    }
}
