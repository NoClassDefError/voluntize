package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.repository.ActivityRepository;
import cn.ncepu.voluntize.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public String createOrUpdate(Activity activity) {
        return activityRepository.save(activity).getId();
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
        if(activityRepository.findById(id).isPresent()){
            Activity activity = activityRepository.findById(id).get();
            if(activity.getStatus().equals(Activity.ActivityStatus.CONFIRMING))
                activity.setStatus(Activity.ActivityStatus.SEND);
            activityRepository.save(activity);
        }
    }
}
