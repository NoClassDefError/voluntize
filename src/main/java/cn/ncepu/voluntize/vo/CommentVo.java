package cn.ncepu.voluntize.vo;

import cn.ncepu.voluntize.entity.Comment;
import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentVo {
    private String id;
    private String time;
    private String parentCommentId;
    private String content;
    private UserInfoVo distributor;
    private String activityId;
    private List<ImageVo> images = new ArrayList<>();

    public CommentVo(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.parentCommentId = comment.getParentComment().getId();
        this.activityId = comment.getActivity().getId();
        this.distributor = new UserInfoVo(comment.getStudent() != null ? 2 : 1, comment.getStudent(), comment.getDepartment());
        this.time = comment.getTime().toString();
        for (Image image : comment.getImages()) images.add(new ImageVo(image));
    }

    //toComment要在service层里写
}
