package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.Comment;
import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.repository.ActivityRepository;
import cn.ncepu.voluntize.repository.CommentRepository;
import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.service.CommentService;
import cn.ncepu.voluntize.vo.requestVo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class CommentImpl implements CommentService {

    @Autowired
    private HttpSession session;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public String saveOrUpdate(CommentVo commentVo) {
        Optional<Comment> comment = commentRepository.findById(commentVo.getId());
        Comment comment1 = comment.orElseGet(Comment::new);
        comment1.setContent(commentVo.getContent());
        final int[] flag = {0};//线程安全
        //判断修改的是否是自己的评论
        if (comment1.getStudent() != null) {
            if (!comment1.getStudent().getStudentNum().equals(session.getAttribute("UserId")))
                flag[0] = 3;
        } else if (comment1.getDepartment() != null)
            if (!comment1.getDepartment().getId().equals(session.getAttribute("UserId")))
                flag[0] = 3;
        comment1.setActivity(activityRepository.findById(commentVo.getActivityId()).orElseGet(() -> {
            flag[0] = 1;
            return null;
        }));
        comment1.setParentComment(commentRepository.findById(commentVo.getParentCommentId()).orElseGet(() -> {
            flag[0] = 2;
            return null;
        }));
        ArrayList<Image> images = new ArrayList<>();
        commentVo.getImageVos().forEach((vo) -> images.add(vo.toImage()));
        comment1.setImages(images);
        comment1.setTime(new Timestamp(new Date().getTime()));
        if ("student".equals(session.getAttribute("userCategory")))
            comment1.setStudent(studentRepository.findById((String) session.getAttribute("UserId")).orElse(null));
        else if ("department".equals(session.getAttribute("userCategory")))
            comment1.setDepartment(departmentRepository.findById((String) session.getAttribute("UserId")).orElse(null));
        switch (flag[0]) {
            default:
                commentRepository.save(comment1);
                return "success";
            case 1:
                return "no activity found";
            case 2:
                return "no parent comment found but result success";
            case 3:
                return "no authority";
        }
    }

    /**
     * 为了防止断链，不删除记录而将其内容改成“该评论已被删除”
     */
    public void delete(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setImages(null);
            comment.setContent("该评论已被删除");
            commentRepository.save(comment);
        }
    }

    /**
     * 查找活动下的所有评论，要求分页查询
     */
    public ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> getComments(String activityId, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByActivityId(activityId, pageable);
        ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> commentVos = new ArrayList<>();
        comments.getContent().forEach((comment -> commentVos.add(new cn.ncepu.voluntize.vo.responseVo.CommentVo(comment))));
        return commentVos;
    }
}
