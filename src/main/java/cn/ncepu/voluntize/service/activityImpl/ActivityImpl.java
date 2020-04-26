package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.*;
import cn.ncepu.voluntize.repository.*;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.ImageVo;
import cn.ncepu.voluntize.vo.requestVo.CreateActivityVo;
import cn.ncepu.voluntize.vo.responseVo.ActivityResponseVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@CacheConfig(cacheNames = "activityService")
public class ActivityImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ActivityStationRepository activityStationRepository;

    @Autowired
    private ActivityPeriodRepository activityPeriodRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ServletContext context;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    private HttpSession session;

    @Override
    @CacheEvict(value = "activityService", allEntries = true)
    public String create2(CreateActivityVo activityVo) {
        Activity activity = new Activity();
        ActivityPeriod activityPeriod = new ActivityPeriod();
        ActivityStation activityStation = new ActivityStation();
        //新增
        if (context.getAttribute("autoSendActivity").equals(true))
            activity.setStatus(Activity.ActivityStatus.SEND);
        else activity.setStatus(Activity.ActivityStatus.CONFIRMING);
        Department department = departmentRepository.findById((String) session.getAttribute("UserId")).orElse(null);
        if (department == null) return "没找到这个部门";
        else
            activity.setDepartment(department);
        activity.setDescription(activityVo.getDescription());
        activity.setName(activityVo.getName());
        if (activityVo.getImageUrl() != null) {
            ArrayList<Image> images = new ArrayList<>();
            Image image = new Image();
            image.setUrl(activityVo.getImageUrl());
            images.add(image);
            activity.setImages(images);
        }

        //添加定时器
        Runnable task = () -> {
            activity.setStatus(Activity.ActivityStatus.SEND);
            activityRepository.save(activity);
            LoggerFactory.getLogger(this.getClass()).info("Task executed, starting activity. Id=" + activity.getId());
        };
        scheduledExecutorService.schedule(task, 3, TimeUnit.DAYS);
        Activity ac = activityRepository.save(activity);

        activityStation.setLinkman(activityVo.getLinkman());
        activityStation.setParentActivity(ac);
        activityStation.setName(activityVo.getStationName());
        activityStation.setPhoneNum(activityVo.getPhoneNum());
        ActivityStation station = activityStationRepository.save(activityStation);

        activityPeriod.setTimePeriod(activityVo.getTimePeriod());
        activityPeriod.setEndDate(Timestamp.valueOf(activityVo.getEndDate()));
        activityPeriod.setStartDate(Timestamp.valueOf(activityVo.getStartDate()));
        activityPeriod.setTimePeriod(activityVo.getTimePeriod());
        activityPeriod.setEquDuration(activityVo.getEquDuration());
        activityPeriod.setAmountRequired(activityVo.getAmountRequired());
        activityPeriod.setRequirements(activityVo.getRequirements());
        activityPeriod.setParent(station);

        activityPeriodRepository.save(activityPeriod);
        return "success";
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public String update(ActivityVo activity) {
        if (activity.getId() != null) {
            Optional<Activity> optional = activityRepository.findById(activity.getId());
            if (optional.isPresent())
                return activityRepository.save(convertActivityVo(optional.get(), activity)).getId();
        }
        return "not found";
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public String updateStation(ActivityStationVo activityStationVo) {
        if (activityStationVo.getId() == null)
            return activityStationRepository.save(
                    convertActivityStationVo(new ActivityStation(), activityStationVo)).getId();
        return activityStationRepository.save(convertActivityStationVo(
                activityStationRepository.findById(activityStationVo.getId())
                        .orElse(new ActivityStation()), activityStationVo)).getId();
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public String updatePeriod(ActivityPeriodVo activityPeriodVo) {
        if (activityPeriodVo.getId() == null)
            return activityPeriodRepository.save(
                    convertActivityPeriodVo(new ActivityPeriod(), activityPeriodVo)).getId();
        return activityPeriodRepository.save(convertActivityPeriodVo(
                activityPeriodRepository.findById(activityPeriodVo.getId())
                        .orElse(new ActivityPeriod()), activityPeriodVo)).getId();
    }

    private Activity convertActivityVo(Activity origin, ActivityVo activity) {
        origin.setName(activity.getName());
        origin.setDescription(activity.getDescription());
        origin.setStatusId(activity.getStatus());
        if (activity.getDepartmentId() == null) origin.setDepartment(null);
        if (departmentRepository.findById(activity.getDepartmentId()).isPresent())
            origin.setDepartment(departmentRepository.findById(activity.getDepartmentId()).get());
        ArrayList<Image> images = new ArrayList<>();
        for (ImageVo imageVo : activity.getImages()) images.add(imageVo.toImage());
        origin.setImages(images);
//        ArrayList<ActivityStation> activityStations = new ArrayList<>();
//        activity.getStations().forEach(stationVo -> activityStations.add(convertActivityStationVo(new ActivityStation(), stationVo)));
//        origin.setStations(activityStations);
        return origin;
    }

    private ActivityStation convertActivityStationVo(ActivityStation station, ActivityStationVo stationVo) {
        station.setDescription(stationVo.getDescription());
        station.setLinkman(stationVo.getLinkman());
        station.setName(stationVo.getName());
        station.setPhoneNum(stationVo.getPhoneNum());

        station.setParentActivity(activityRepository.findById(stationVo.getParentId()).orElse(null));
//        ArrayList<ActivityPeriod> activityPeriods = new ArrayList<>();
//        stationVo.getPeriods().forEach(activityPeriodVo -> activityPeriods.add(convertActivityPeriodVo(new ActivityPeriod(), activityPeriodVo)));
//        station.setPeriods(activityPeriods);
        return station;
    }

    private ActivityPeriod convertActivityPeriodVo(ActivityPeriod period, ActivityPeriodVo periodVo) {
        period.setAmountRequired(periodVo.getAmountRequired());
        System.out.println(periodVo.getEndDate() + periodVo.getStartDate() + periodVo);
        period.setEndDate(Timestamp.valueOf(periodVo.getEndDate()));
        period.setStartDate(Timestamp.valueOf(periodVo.getStartDate()));
        period.setEquDuration(periodVo.getEquDuration());
        period.setParent(activityStationRepository.findById(periodVo.getParentStationId()).orElse(null));

        period.setRequirements(periodVo.getRequirements());
        period.setTimePeriod(periodVo.getTimePeriod());
        // TODO 这里不setRecords，更新activity时，Hibernate会删除关联吗？猜测是会，因为period主键id已经改变
        return period;
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public void deleteActivity(String id) {
//        System.out.println(id);
        activityRepository.deleteById(id);
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public void deleteActivityPeriod(String id) {
        activityPeriodRepository.deleteById(id);
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public void deleteActivityStation(String id) {
        activityStationRepository.deleteById(id);
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public List<ActivityVo> findStatus(Activity.ActivityStatus status, Pageable pageable) {
//      放弃使用QBE的方法
//        Activity activity = new Activity();
//        activity.setStatus(status[0]);
//        Example<Activity> example = Example.of(activity);
        System.out.println("ActivityImpl$findStatus:status==" + status);
        if (status == null) return activityRepository.findByStatus(pageable);
//        return new MyPageImpl<>(page1.getContent(), page1.getPageable());
        return activityRepository.findByStatus(status.ordinal(), pageable);
    }

    /**
     * 给学生主页活动查询用的，只查询报名阶段的和进行中阶段的
     */
    @Override
    public List<ActivityVo> findStatusSpecial(Pageable pageable) {
        return activityRepository.findByStatus(pageable);
    }

    @Override
    public List<ActivityVo> notToFindStatus(Activity.ActivityStatus status, Pageable pageable) {
        return activityRepository.notToFindByStatus(status.ordinal(), pageable);
    }

    /**
     * /department/released接口所调用的
     * status == 6 表示状态为123的活动
     * status == 7 表示所有活动
     */
    @Override
    @Cacheable(value = "activityService", key = "'depRls:'+#p0+','+#p1+','+#p2.hashCode()")
    public List<ActivityResponseVo> findDepartment(String departmentId, Integer status, Pageable pageable) {
//        System.out.println(status+" "+page);
        if (status == null || status == 7)
            return activityRepository.findByDepartmentId(departmentId, pageable);
        else if (status == 6)
            return activityRepository.findByDepartmentIdSpecial(departmentId, pageable);
        else return activityRepository.findByDepartmentId(departmentId, status, pageable);
    }

    @Override
    public Activity findById(String activityId) {
        return activityRepository.findById(activityId).orElse(null);
    }

    @Override
    public Activity findByPeriod(String periodId) {
        Optional<ActivityPeriod> period = activityPeriodRepository.findById(periodId);
        return period.map(activityPeriod -> activityPeriod.getParent().getParentActivity()).orElse(null);
    }

    @Override
    public Activity findByStation(String stationId) {
        return activityStationRepository.findById(stationId).map(ActivityStation::getParentActivity).orElse(null);
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public String startActivity(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity != null) {
            activity.setStatus(Activity.ActivityStatus.STARTED);
            for (ActivityStation station : activity.getStations())
                for (ActivityPeriod period : station.getPeriods())
                    for (Record record : period.getRecords())
                        if (record.getStatusId() == 0) {
                            record.setPassed(false);
                            recordRepository.save(record);
                        }
            activityRepository.save(activity);
            return "success";
        } else return "not found";
    }

    @CacheEvict(value = "activityService", allEntries = true)
    @Override
    public String changeStatus(String activityId, Activity.ActivityStatus status) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity != null) {
            activity.setStatus(status);
            activityRepository.save(activity);
            return "success";
        } else return "not found";
    }
}
