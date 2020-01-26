package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Comment;
import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.vo.ImageVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 仅负责response，requestVo下还有一个CommentVo
 */
@Data
public class CommentVo {
    private String id;
    private String time;
    private String parentCommentId;
    private String content;
    private String distributorCategory;
    private String distributorName;
    private String distributorId;
    private String activityId;
    private List<ImageVo> images = new ArrayList<>();

    public CommentVo(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.parentCommentId = comment.getParentComment() == null ? null : comment.getParentComment().getId();
        this.activityId = comment.getActivity().getId();
//        this.distributor = new UserInfoVo(comment.getStudent() != null ? 2 : 1, comment.getStudent(), comment.getDepartment());
        this.distributorCategory = comment.getStudent() != null ? "student" : "department";
        this.distributorId = this.distributorCategory.equals("student") ? comment.getStudent().getStudentNum() : comment.getDepartment().getId();
        this.distributorName = this.distributorCategory.equals("student") ? comment.getStudent().getName() : comment.getDepartment().getName();
        this.time = comment.getTime().toString();
        for (Image image : comment.getImages()) images.add(new ImageVo(image));
    }
}
