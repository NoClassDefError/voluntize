package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {
    @Query("select a from Activity a where a.department.id=?1")
    List<Activity> findByDepartmentId(String departmentId);

    @Query("select a from Activity a where a.department.id=?1 and a.statusId=?2")
    List<Activity> findByDepartmentId(String departmentId, int status);
}
