package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Student;
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

    @Override
    List<Student> findAll();

    @Transactional
    @Modifying
    @Query("update Student s set s.totalDuration=?2 where s.studentNum=?1")
    void updateTotalDuration(String id,int duration);
}
