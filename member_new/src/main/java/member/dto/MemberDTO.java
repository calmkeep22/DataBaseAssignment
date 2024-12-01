package member.dto;

import lombok.*;
import member.domain.CourseEntity;
import member.domain.MemberEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String memberDepartment;
    private List<CourseDTO> courses = new ArrayList<>();
    private List<BoardDTO> boards = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();  // 댓글 리스트 추가
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberDepartment(memberEntity.getMemberDepartment());
        return memberDTO;
    }
}
