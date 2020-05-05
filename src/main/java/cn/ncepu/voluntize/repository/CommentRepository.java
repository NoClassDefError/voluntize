package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    //默认不用原生sql语句，不能用*号
    @Query(value = "select c from Comment c where c.activity.id=?1 order by c.time")
    Page<Comment> findByActivityId(String activity, Pageable pageable);

    @Query("select c from Comment c where c.activity is null")
    List<Comment> findIndexComment();
}
