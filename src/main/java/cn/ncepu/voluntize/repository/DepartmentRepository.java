package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    //sql server中avg()返回的类型与括号中一致，要先转成浮点型
    @Query(value = "select new cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo(d.id, d.name, avg(r.stars*1.0))" +
            " from Department d left join Activity a on d.id = a.department " +
            " left join ActivityStation ast on a.id = ast.parentActivity " +
            " left join ActivityPeriod ap on ast.id = ap.parent " +
            " left join Record r on ap.id = r.period " +
            "where r.statusId = 3 and d.isDeleted=false " +
            "group by d.id, d.name")
        // 注意sql server 规定，如果 SELECT 子句 <select list> 中包含聚合函数，则 GROUP BY 将计算每个组的汇总值。
        // 指定 GROUP BY 时，选择列表中任何非聚合表达式内的每个属性名都应包含在GROUP BY列表中，或者GROUP BY表达式必须与选择列表表达式完全匹配
    List<DepartmentExcelVo> getDepartmentExcelVo();

    @Transactional
    @Modifying
    @Query("update Department d set d.isDeleted=true,d.name='账号已注销',d.manager='已注销'," +
            "d.phoneNum='',d.email='' where d.id=?1")
    void closeDepartment(String id);

    @Override
    @Query("select d from Department d where d.id=?1 and d.isDeleted=false")
    Optional<Department> findById(String id);

    @Query("select d from Department d where d.id=?1 and d.isDeleted=true")
    Optional<Department> findByIdCanceled(String id);
}
