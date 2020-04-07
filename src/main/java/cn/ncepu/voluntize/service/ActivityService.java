package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.requestVo.CreateActivityVo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityService {
    String create2(CreateActivityVo activityVo);
    /**
     * 添加或更新志愿活动
     *
     * @param activity 保存的activity
     * @return 保存activity的Id
     */
    String update(ActivityVo activity);

    String updateStation(ActivityStationVo activityStationVo);

    String updatePeriod(ActivityPeriodVo activityPeriodVo);

    void deleteActivity(String id);

    void deleteActivityPeriod(String id);

    void deleteActivityStation(String id);

    List<Activity> findAll();

    /**
     * 查找特定阶段的活动
     *
     * @param status 活动阶段
     * @return 活动
     */
    Page<Activity> findStatus(Activity.ActivityStatus status, int page);

    List<Activity> findStatus(Activity.ActivityStatus status);

    Page<Activity> notToFindStatus(Activity.ActivityStatus status, int page, int size);

    String startActivity(String activityId);

    String changeStatus(String activityId, Activity.ActivityStatus status);

    /**
     * 在假设活动与时间段一一对应时
     */
    Page<Activity> findDepartment(String departmentId, Integer status, int page);

    Activity findById(String activityId);

    Activity findByPeriod(String periodId);

    Activity findByStation(String stationId);
}
