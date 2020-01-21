package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.ActivityStation;
import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.repository.ActivityRepository;
import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.ImageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public String createOrUpdate(ActivityVo activity) {
        Optional<Activity> optional = activityRepository.findById(activity.getId());
        if (optional.isPresent())
            return activityRepository.save(convertActivityVo(optional.get(), activity)).getId();
        else return activityRepository.save(convertActivityVo(new Activity(), activity)).getId();
    }

    private Activity convertActivityVo(Activity origin, ActivityVo activity) {
        origin.setStatus(activity.getStatus());
        origin.setName(activity.getName());
        origin.setSemester(activity.getSemester());
        origin.setDescription(activity.getDescription());
        if (departmentRepository.findById(activity.getDepartmentId()).isPresent())
            origin.setDepartment(departmentRepository.findById(activity.getDepartmentId()).get());
        ArrayList<Image> images = new ArrayList<>();
        for (ImageVo imageVo : activity.getImages()) images.add(imageVo.toImage());
        origin.setImages(images);

        return origin;
    }

    private ActivityStation convertActivityStationVo(ActivityStation station, ActivityStationVo stationVo) {
        station.setDescription(stationVo.getDescription());
        station.setLinkman(stationVo.getLinkman());
        station.setName(stationVo.getName());
        station.setPhoneNum(stationVo.getPhoneNum());

        //TODO 转换Activity
        return null;
    }

    @Override
    public void deleteActivity(String id) {
        activityRepository.deleteById(id);
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public List<Activity> findStatus(Activity.ActivityStatus status) {
        Activity activity = new Activity();
        activity.setStatus(status);
        Example<Activity> example = Example.of(activity);
        return activityRepository.findAll(example);
    }

    @Deprecated
    public void confirm(String id) {
        if (activityRepository.findById(id).isPresent()) {
            Activity activity = activityRepository.findById(id).get();
            if (activity.getStatus().equals(Activity.ActivityStatus.CONFIRMING))
                activity.setStatus(Activity.ActivityStatus.SEND);
            activityRepository.save(activity);
        }
    }

    @Override
    public List<Activity> findDepartment(String departmentId){
        Optional<Department> department = departmentRepository.findById(departmentId);
        if(department.isPresent()){
            Department department1 = department.get();
            return department1.getActivities();
        }
        return null;
    }
}
