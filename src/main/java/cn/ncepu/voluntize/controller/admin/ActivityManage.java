package cn.ncepu.voluntize.controller.admin;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import cn.ncepu.voluntize.vo.responseVo.RecordVoDpm;
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
    private ParticipateService participateService;

    @Autowired
    private ServletContext context;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveActivity(@RequestBody ActivityVo activity) {
        return new HttpResult("saveActivity:", activityService.update(activity));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteActivity(String id) {
        activityService.deleteActivity(id);
    }

    @RequestMapping("/autoSendActivity")
    @ResponseBody
    public HttpResult setAutoSendActivity(String autoSendActivity) {
        boolean flag = "true".equals(autoSendActivity);
        context.setAttribute("autoSendActivity", flag);
        return new HttpResult("AutoSendActivityEnable:" + autoSendActivity);
    }

    @RequestMapping("/confirm")
    @ResponseBody
    public HttpResult confirm(String activityId) {
        return new HttpResult("confirm:" +
                activityService.changeStatus(activityId, Activity.ActivityStatus.SEND));
    }

    @RequestMapping("/deConfirm")
    @ResponseBody
    public HttpResult deConfirm(String activityId) {
        return new HttpResult("deConfirm:" +
                activityService.changeStatus(activityId, Activity.ActivityStatus.NOT_PASS));
    }


    @RequestMapping(value = "/findConfirming", method = RequestMethod.POST)
    @ResponseBody
    public List<ActivityVo> findConfirmingActivities(Integer page) {
        if (page == null) page = 0;
        if ((boolean) context.getAttribute("autoSendActivity")) return null;
        List<ActivityVo> activityVos = new ArrayList<>();
        for (Activity activity : activityService.findStatus(Activity.ActivityStatus.CONFIRMING, page))
            activityVos.add(new ActivityVo(activity));
//        System.out.println(activityVos);
        return activityVos;
    }

    @RequestMapping("/findOthers")
    @ResponseBody
    public List<ActivityVo> findOthers(Integer page) {
        if (page == null) page = 0;
        List<ActivityVo> activityVos = new ArrayList<>();
        for (Activity activity : activityService.notToFindStatus(Activity.ActivityStatus.CONFIRMING, page, 10))
            activityVos.add(new ActivityVo(activity));
        return activityVos;
    }

    @RequestMapping(value = "/records", method = RequestMethod.POST)
    public List<RecordVoDpm> getRecords(String activityId) {
//        System.out.println(periodId);
        if (activityService.findById(activityId) != null) {
            ArrayList<RecordVoDpm> recordVos = new ArrayList<>();
            try {
                for (Record record : participateService.getRecord(
                        activityService.findById(activityId).getStations().get(0).getPeriods().get(0).getId()))
                    recordVos.add(new RecordVoDpm(record));
                return recordVos;
            } catch (NullPointerException e) {
                logger.error("The activity " + activityId + " does not have it's own station or period, NullPointerException thrown.");
                return null;
            }
        } else return null;
    }
}
