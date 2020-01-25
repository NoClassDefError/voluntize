package cn.ncepu.voluntize.controller.admin;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于管理员与部门发布和管理志愿活动
 */
@RestController("/admin")
public class ActivityManage extends BaseController {
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/saveActivity", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveActivity(@RequestBody ActivityVo activity) {
        return new HttpResult("saveActivity:", activityService.createOrUpdate(activity));
    }

    @RequestMapping(value = "/deleteActivity", method = RequestMethod.POST)
    public void deleteActivity(String id) {
        activityService.deleteActivity(id);
    }

    /**
     * 查询之前需要使用session，判断管理员身份
     *
     * @return json
     */
    @RequestMapping(value = "/findAllAc", method = RequestMethod.POST)
    @ResponseBody
    public List<ActivityVo> findAllActivities() {
        List<ActivityVo> activityVos = new ArrayList<>();
        for (Activity activity : activityService.findAll())
            activityVos.add(new ActivityVo(activity));
        return activityVos;
    }

    @RequestMapping(value = "/findConfirmingAc", method = RequestMethod.POST)
    @ResponseBody
    public List<ActivityVo> findConfirmingActivities() {
        List<ActivityVo> activityVos = new ArrayList<>();
        for (Activity activity : activityService.findStatus(Activity.ActivityStatus.CONFIRMING))
            activityVos.add(new ActivityVo(activity));
        return activityVos;
    }
}
