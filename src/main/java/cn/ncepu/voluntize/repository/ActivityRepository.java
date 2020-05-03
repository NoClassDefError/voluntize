package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.responseVo.ActivityResponseVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 查出来的不要是实体类，那样还要自行转换成vo，直接在Hql里自定义查询结果
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {
    @Query("select new cn.ncepu.voluntize.vo.responseVo.ActivityResponseVo(a) from Activity a where a.department.id=?1 order by a.createTime desc")
    List<ActivityResponseVo> findByDepartmentId(String departmentId, Pageable pageable);

    @Query("select new cn.ncepu.voluntize.vo.responseVo.ActivityResponseVo(a) from Activity a where a.department.id=?1 and a.statusId=?2 order by a.createTime desc")
    List<ActivityResponseVo> findByDepartmentId(String departmentId, int status, Pageable pageable);

    @Query("select new cn.ncepu.voluntize.vo.responseVo.ActivityResponseVo(a) from Activity a where a.department.id=?1 and a.statusId<4 order by a.createTime desc")
    List<ActivityResponseVo> findByDepartmentIdSpecial(String departmentId, Pageable pageable);

    @Query("select new cn.ncepu.voluntize.vo.ActivityVo(a) from Activity a where a.statusId=:status order by a.createTime desc")
    List<ActivityVo> findByStatus(@Param("status") int status, Pageable pageable);

    @Query("select new cn.ncepu.voluntize.vo.ActivityVo(a) from Activity a where a.statusId=1 or a.statusId=2 order by a.createTime desc")
    List<ActivityVo> findByStatus(Pageable pageable);

    @Query("select new cn.ncepu.voluntize.vo.ActivityVo(a) from Activity a where a.statusId<>?1 order by a.createTime desc")
    List<ActivityVo> notToFindByStatus(int notStatus, Pageable pageable);
}
