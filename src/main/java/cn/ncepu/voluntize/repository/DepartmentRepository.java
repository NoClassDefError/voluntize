package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,String> {
}
