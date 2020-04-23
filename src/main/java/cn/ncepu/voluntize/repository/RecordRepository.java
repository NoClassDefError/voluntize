package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo;
import cn.ncepu.voluntize.vo.responseVo.RecordVoDpm;
import cn.ncepu.voluntize.vo.responseVo.RecordVoStu;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {
    @Query("select new cn.ncepu.voluntize.vo.responseVo.RecordVoStu(r) from Record r where r.statusId=?2 and r.volunteer.studentNum=?1 order by r.createTime")
    List<RecordVoStu> findByStudent(String studentId, int status);

    @Query("select new cn.ncepu.voluntize.vo.responseVo.RecordVoStu(r) from Record r where r.volunteer.studentNum=?1 order by r.createTime")
    List<RecordVoStu> findByStudent(String studentId);

    @Query("select new cn.ncepu.voluntize.vo.responseVo.RecordVoDpm(r) from Record r where r.period.id=?1 and r.statusId=?2 order by r.createTime")
    List<RecordVoDpm> findByPeriod(String period, int status);

    @Query("select new cn.ncepu.voluntize.vo.responseVo.RecordVoDpm(r) from Record r where r.period.id=?1 order by r.createTime")
    List<RecordVoDpm> findByPeriod(String period);

    @Query("select count(r) from Record r where r.period.id=?1 and r.isPassed=true")
    Integer getAmountPassed(String period);

    @Query("select new cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo(r.period.parent.parentActivity.department.id," +
            "r.period.parent.parentActivity.department.name,avg(r.stars)) from Record r " +
            "where r.period.parent.parentActivity.department.id=?1 and r.statusId=3")
//    @Query(nativeQuery = true, value = "select avg( voluntize.record.stars) from voluntize.department " +
//            "left join voluntize.activity on voluntize.department.id =voluntize.activity.department " +
//            "left join voluntize.activity_station on voluntize.activity.id =voluntize.activity_station.parent_activity " +
//            "left join voluntize.activity_period on voluntize.activity_station.id = voluntize.activity_period.parent " +
//            "left join voluntize.record on voluntize.activity_period.id = voluntize.record.the_period " +
//            "where voluntize.record.status_id = 3  and voluntize.department.id = ?1")
    DepartmentExcelVo getAveStarForDepartment(String depId);

}