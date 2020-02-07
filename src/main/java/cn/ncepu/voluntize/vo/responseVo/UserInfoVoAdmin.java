package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.vo.ActivityVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfoVoAdmin {
    private int userCategory = -1;
    private StudentVo student = null;
    private DepartmentVo department = null;
    private List<RecordVoStu> stuRecords = new ArrayList<>();
    private List<ActivityVo> depActivities = new ArrayList<>();

    public UserInfoVoAdmin(int userCategory, Student student, Department department) {
        this.student = student == null ? null : new StudentVo(student);
        this.department = department == null ? null : new DepartmentVo(department);
        this.userCategory = userCategory;
        if (student != null)
            for (Record record : student.getParticipated())
                stuRecords.add(new RecordVoStu(record));
        if (department != null)
            for (Activity activity : department.getActivities())
                depActivities.add(new ActivityVo(activity));
    }

}
