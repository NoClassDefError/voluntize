package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.vo.responseVo.StudentExcelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,String> {

    @Query("select new cn.ncepu.voluntize.vo.responseVo.StudentExcelVo(s) from Student s where s.grade=?1")
    List<StudentExcelVo> findExcelByGrade(int grade);

    @Query("select new cn.ncepu.voluntize.vo.responseVo.StudentExcelVo(s) from Student s ")
    List<StudentExcelVo> findExcelAll();

    @Transactional
    @Modifying
    @Query("update Student s set s.totalDuration=?2 where s.studentNum=?1")
    void updateTotalDuration(String id,int duration);
}
