package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
//    @Query(value = "select new cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo(d.id,d.name, avg(r.stars))" +
//            "from Department d" +
//            "         left join Activity on Department.id = Activity.department" +
//            "         left join ActivityStation on Activity.id = ActivityStation.parentActivity " +
//            "         left join ActivityPeriod on ActivityStation.id = ActivityPeriod.parent " +
//            "         left join Record r on ActivityPeriod.id = Record.period " +
//            "where r.statusId = 3 " +
//            "group by d.id")
//    List<DepartmentExcelVo> getDepartmentExcelVo();
}
