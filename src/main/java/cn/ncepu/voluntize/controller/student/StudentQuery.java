package cn.ncepu.voluntize.controller.student;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.responseVo.RecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 除了login接口返回的基本信息外，学生主页上所要展示的其他信息
 */
@RestController("/student/query")
public class StudentQuery extends BaseController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ParticipateService participateService;

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

    /**
     * 学生在自己主页上获取自己参加的公益来动记录，后期可以增加分页功能
     */
    @RequestMapping(value = "/getRecord", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<RecordVo> getStuRecord(String periodId, String status) {
        ArrayList<RecordVo> recordVos = new ArrayList<>();
        for (Record record : participateService.getRecord(periodId, Record.RecordStatus.valueOf(status)))
            recordVos.add(new RecordVo(record));
        return recordVos;
    }
}
