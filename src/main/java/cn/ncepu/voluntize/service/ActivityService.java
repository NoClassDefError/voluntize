package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;

import java.util.List;

public interface ActivityService {

    /**
     * 添加或更新志愿活动
     *
     * @param activity 保存的activity
     * @return 保存activity的Id
     */
    String createOrUpdate(ActivityVo activity);

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
    List<Activity> findStatus(Activity.ActivityStatus status);

    /**
     * 在假设活动与时间段一一对应时
     */
    List<Activity> findDepartment(String departmentId, Integer status);

    Activity findById(String activityId);

    Activity findByPeriod(String periodId);

    Activity findByStation(String stationId);
}
