package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    @Query(value = "select new cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo(d.id, d.name, avg(r.stars))" +
            " from Department d" +
            " left join Activity a on d.id = a.department " +
            " left join ActivityStation ast on a.id = ast.parentActivity " +
            " left join ActivityPeriod ap on ast.id = ap.parent " +
            " left join Record r on ap.id = r.period " +
            "where r.statusId = 3 " +
            "group by d.id")
    List<DepartmentExcelVo> getDepartmentExcelVo();
}
