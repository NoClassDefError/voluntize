package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.ActivityPeriod;
import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.ActivityPeriodRepository;
import cn.ncepu.voluntize.repository.RecordRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.requestVo.ParticipateVo;
import cn.ncepu.voluntize.service.ParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ParticipateImpl implements ParticipateService {

    @Autowired
    private HttpSession session;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ActivityPeriodRepository activityPeriodRepository;

    /**
     * 添加多对多关联，不需要更新Student表与ActivityPeriod表
     */
    @Override
    public Record participate(ParticipateVo participateVo) {
        Optional<ActivityPeriod> activityPeriod = activityPeriodRepository.findById(participateVo.getActivityId());
        Optional<Student> student = studentRepository.findById((String) session.getAttribute("UserId"));
        if (student.isPresent() && activityPeriod.isPresent()) {
            Record record = new Record();
            record.setVolunteer(student.get());
            record.setPeriod(activityPeriod.get());
            record.setInfo(participateVo.getInfo());
            record.setStatus(Record.RecordStatus.APPLIED);
            return recordRepository.save(record);
        }
        return null;
    }

    @Override
    public ArrayList<Record> getRecord(String periodId, Record.RecordStatus status) {
        Optional<ActivityPeriod> activityPeriod = activityPeriodRepository.findById(periodId);
        if (activityPeriod.isPresent()) {
            ArrayList<Record> result = new ArrayList<>();
            for (Record record : activityPeriod.get().getRecords())
                if (record.getStatus().equals(status)) result.add(record);
            return result;
        }
        return null;
    }

    @Override
    public void accept(List<String> records) {
        for (String id : records) {
            Optional<Record> record = recordRepository.findById(id);
            if (record.isPresent()) {
                record.get().setStatus(Record.RecordStatus.PASSED);
                recordRepository.save(record.get());
            }
        }
    }

    @Override
    public void evaluate(Map<String, Integer> records) {
        for (Map.Entry<String, Integer> entry : records.entrySet()) {
            Optional<Record> record = recordRepository.findById(entry.getKey());
            if (record.isPresent()) {
                record.get().setStatus(Record.RecordStatus.EVALUATED);
                record.get().setAuditLevel(entry.getValue());
                recordRepository.save(record.get());
            }
        }
    }

}
