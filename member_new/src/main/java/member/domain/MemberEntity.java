package member.domain;

import lombok.Getter;
import lombok.Setter;
import member.dto.MemberDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String memberDepartment;

    @ManyToMany(mappedBy = "memberEntities", fetch = FetchType.LAZY)
    private List<CourseEntity> courseEntities;
    //추가코드
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntities = new ArrayList<>();

    // 추가된 부분: 댓글과 회원의 관계 설정
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntities = new ArrayList<>();

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberDepartment(memberDTO.getMemberDepartment());
        return memberEntity;
    }

    public static MemberEntity toUpdateMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberDepartment(memberDTO.getMemberDepartment());
        return memberEntity;
    }

    public void addCourse(CourseEntity course) {
        if (this.courseEntities == null) {
            this.courseEntities = new ArrayList<>();
        }
        if (!this.courseEntities.contains(course)) {
            this.courseEntities.add(course);
        }
        if (!course.getMemberEntities().contains(this)) {
            course.getMemberEntities().add(this);
        }
    }

    public void removeCourse(CourseEntity course) {
        if (this.courseEntities != null && this.courseEntities.contains(course)) {
            this.courseEntities.remove(course);
        }
        if (course.getMemberEntities() != null && course.getMemberEntities().contains(this)) {
            course.getMemberEntities().remove(this);
        }
    }
    //추가코드
    public void addBoard(BoardEntity board) {
        boardEntities.add(board);
        board.setMemberEntity(this);  // 양방향 관계 설정
    }
    public void addComment(CommentEntity commentEntity) {
        commentEntities.add(commentEntity);
        commentEntity.setMemberEntity(this);  // 양방향 관계 설정
    }
}

