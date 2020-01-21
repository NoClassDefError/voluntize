package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    //默认不用原生sql语句，不能用*号
    @Query(value = "select Comment from Comment where activity=?1")
    Page<Comment> findByActivityId(String activity, Pageable pageable);
}
