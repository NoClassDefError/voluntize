package cn.ncepu.voluntize.controller.admin;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于管理员与部门发布和管理志愿活动
 */
@RestController
@RequestMapping("/admin/activity")
public class ActivityManage extends BaseController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ServletContext context;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveActivity(@RequestBody ActivityVo activity) {
        return new HttpResult("saveActivity:", activityService.createOrUpdate(activity));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteActivity(String id) {
        activityService.deleteActivity(id);
    }

    @RequestMapping("/autoSendActivity")
    @ResponseBody
    public HttpResult setAutoSendActivity(boolean autoSendActivity) {
        context.setAttribute("autoSendActivity", autoSendActivity);
        return new HttpResult("AutoSendActivityEnable:" + autoSendActivity);
    }

    @RequestMapping("/confirm")
    @ResponseBody
    public HttpResult confirm(String activityId) {
        return new HttpResult("confirm:" +
                activityService.changeStatus(activityId, Activity.ActivityStatus.SEND));
    }



    @RequestMapping(value = "/findConfirming", method = RequestMethod.POST)
    @ResponseBody
    public List<ActivityVo> findConfirmingActivities() {
        if((boolean) context.getAttribute("autoSendActivity")) return null;
        List<ActivityVo> activityVos = new ArrayList<>();
        for (Activity activity : activityService.findStatus(Activity.ActivityStatus.CONFIRMING))
            activityVos.add(new ActivityVo(activity));
        return activityVos;
    }

    @RequestMapping("/findOthers")
    @ResponseBody
    public List<ActivityVo> findOthers(int page) {
        List<ActivityVo> activityVos = new ArrayList<>();
        for (Activity activity : activityService.notToFindStatus(Activity.ActivityStatus.CONFIRMING, page, 10))
            activityVos.add(new ActivityVo(activity));
        return activityVos;
    }

}
