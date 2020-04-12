package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {
    @Query("select a from Activity a where a.department.id=?1 order by a.createTime")
    Page<Activity> findByDepartmentId(String departmentId, Pageable pageable);

    @Query("select a from Activity a where a.department.id=?1 and a.statusId=?2 order by a.createTime")
    Page<Activity> findByDepartmentId(String departmentId, int status, Pageable pageable);

    @Query("select a from Activity a where a.department.id=?1 and a.statusId<4 order by a.createTime")
    Page<Activity> findByDepartmentIdSpecial(String departmentId, Pageable pageable);

    @Query("select a from Activity a where a.statusId=:status order by a.createTime")
    Page<Activity> findByStatus(@Param("status") int status, Pageable pageable);

    @Query("select a from Activity a where a.statusId=:status order by a.createTime")
    List<Activity> findByStatus2(@Param("status") int status);

    @Query("select a from Activity a where a.statusId<>?1 order by a.createTime")
    Page<Activity> notToFindByStatus(int notStatus, Pageable pageable);
}
