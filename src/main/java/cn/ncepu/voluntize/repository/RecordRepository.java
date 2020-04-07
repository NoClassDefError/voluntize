package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {
    @Query("select r from Record r where r.statusId=?2 and r.volunteer.studentNum=?1 order by r.createTime")
    List<Record> findByStudent(String studentId, int status);

    @Query("select r from Record r where r.volunteer.studentNum=?1 order by r.createTime")
    List<Record> findByStudent(String studentId);

    @Query("select r from Record r where r.period.id=?1 and r.isPassed=true order by r.createTime")
    List<Record> findPassedByPeriod(String period);
}
