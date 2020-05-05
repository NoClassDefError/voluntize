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
import java.sql.Time;
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
        Optional<Comment> comment = commentRepository.findById(commentVo.getId() != null ? commentVo.getId() : "");
        Comment comment1 = comment.orElseGet(Comment::new);
        comment1.setTime(System.currentTimeMillis());
        comment1.setContent(commentVo.getContent());
        final int[] flag = {0};//线程安全
        comment1.setActivity(activityRepository.findById(commentVo.getActivityId() != null ? commentVo.getActivityId() : "").orElseGet(() -> {
            flag[0] = 1;
            return null;
        }));
        comment1.setParentComment(commentRepository.findById(commentVo.getParentCommentId() != null ? commentVo.getParentCommentId() : "").orElseGet(() -> {
            flag[0] = 2;
            return null;
        }));
        //判断修改的是否是自己的评论
        if (comment1.getStudent() != null) {
            if (!comment1.getStudent().getStudentNum().equals(session.getAttribute("UserId")))
                flag[0] = 3;
        } else if (comment1.getDepartment() != null){
            if (!comment1.getDepartment().getId().equals(session.getAttribute("UserId")))
                flag[0] = 3;
        }else if (!session.getAttribute("UserCategory").equals("Admin"))
            flag[0] = 3;

        ArrayList<Image> images = new ArrayList<>();
        if (commentVo.getImageVos() != null) commentVo.getImageVos().forEach((vo) -> images.add(vo.toImage()));
        comment1.setImages(images);
        if ("Student".equals(session.getAttribute("UserCategory")))
            comment1.setStudent(studentRepository.findById((String) session.getAttribute("UserId")).orElse(null));
        else if ("Department".equals(session.getAttribute("UserCategory")))
            comment1.setDepartment(departmentRepository.findById((String) session.getAttribute("UserId")).orElse(null));
        switch (flag[0]) {
            default:
                return "success---commentId:" + commentRepository.save(comment1).getId();
            case 1:
                return "no parent activity found, seen as a broadcast.---commentId:"+commentRepository.save(comment1).getId();
            case 2:
                return "no parent comment found but result success---commentId:" + commentRepository.save(comment1).getId();
            case 3:
                return "no authority";
        }
    }

    /**
     * 为了防止断链，不删除记录而将其内容改成“该评论已被删除”
     */
    @Override
    public void delete(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            //权限校验
            if (comment.getStudent() != null) {
                if (!comment.getStudent().getStudentNum().equals(session.getAttribute("UserId")))
                    return;
            } else if (comment.getDepartment() != null){
                if (!comment.getDepartment().getId().equals(session.getAttribute("UserId")))
                    return;
            }else if(!session.getAttribute("UserCategory").equals("Admin"))
                return;
            //删除评论
            comment.setImages(null);
            comment.setContent("该评论已被删除");
            commentRepository.save(comment);
        }
    }

    /**
     * 查找活动下的所有评论，要求分页查询
     */
    @Override
    public ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> getComments(String activityId, Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Comment> comments = commentRepository.findByActivityId(activityId, pageable);
        ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> commentVos = new ArrayList<>();
        comments.getContent().forEach((comment -> commentVos.add(new cn.ncepu.voluntize.vo.responseVo.CommentVo(comment))));
        return commentVos;
    }

    @Override
    public ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> getIndexComments(){
        ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> commentVos = new ArrayList<>();
        commentRepository.findIndexComment().forEach(comment -> commentVos.add(new cn.ncepu.voluntize.vo.responseVo.CommentVo(comment)));
        return commentVos;
    }
}
