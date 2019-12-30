package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Activity;

import java.util.List;

public interface ActivityService {

    /**
     * 添加或更新志愿活动
     * @param activity 保存的activity
     * @return 保存activity的Id
     */
    String createOrUpdate(Activity activity);

    void deleteActivity(String id);

    List<Activity> findAll();

    /**
     * 查找特定阶段的活动
     * @param status 活动阶段
     * @return 活动
     */
    List<Activity> findStatus(Activity.ActivityStatus status);

}
