package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.requestVo.CreateActivityVo;
import cn.ncepu.voluntize.vo.responseVo.ActivityResponseVo;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {
    String create2(CreateActivityVo activityVo);

    String update(ActivityVo activity);

    String updateStation(ActivityStationVo activityStationVo);

    String updatePeriod(ActivityPeriodVo activityPeriodVo);

    void deleteActivity(String id);

    void deleteActivityPeriod(String id);

    void deleteActivityStation(String id);

    List<Activity> findAll();

    List<ActivityVo> findStatus(Activity.ActivityStatus status, Pageable pageable);

    List<ActivityVo> findStatusSpecial(Pageable pageable);

    List<ActivityVo> notToFindStatus(Activity.ActivityStatus status, Pageable pageable);

    String startActivity(String activityId);

    String changeStatus(String activityId, Activity.ActivityStatus status);

    List<ActivityResponseVo> findDepartment(String departmentId, Integer status, Pageable pageable);

    Activity findById(String activityId);

    Activity findByPeriod(String periodId);

    Activity findByStation(String stationId);
}
