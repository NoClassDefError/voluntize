package cn.ncepu.voluntize.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * <h2>志愿评论实体类</h2>
 * 本类用于学生与部门进行志愿活动相关讨论，活动处于报名阶段及之后就可以评论。<br>
 * 评论本身为树状结构，允许对评论进行评论。
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"activity", "parentComment", "sonComment", "images", "department", "student"})
@JsonIgnoreProperties({"activity", "parentComment", "sonComment", "images", "department", "student"})
public class Comment {
    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 所属志愿活动，非空
     */
    @ManyToOne(targetEntity = Activity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity", referencedColumnName = "id")
    private Activity activity;

    @ManyToOne(targetEntity = Student.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "student", referencedColumnName = "id")
    private Student student;

    @ManyToOne(targetEntity = Department.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "department", referencedColumnName = "id")
    private Department department;

    /**
     * 评论内容
     */
    @Basic
    @Column(name = "description", length = 5000)
    private String content;

    /**
     * 发布时间
     */
    @Basic
    @Column(name = "time", nullable = false)
    @CreatedDate
    private Long time;

    /**
     * 评论图片
     */
    @OneToMany(targetEntity = Image.class, mappedBy = "comment", fetch = FetchType.LAZY)
    private List<Image> images;

    /**
     * 父级评论，没有父级则为空
     */
    @ManyToOne(targetEntity = Comment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment", referencedColumnName = "id")
    private Comment parentComment;

    @OneToMany(targetEntity = Comment.class, mappedBy = "parentComment", fetch = FetchType.LAZY)
    private List<Comment> sonComment;
}
