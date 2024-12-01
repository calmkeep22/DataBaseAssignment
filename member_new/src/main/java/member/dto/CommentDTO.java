package member.dto;

import lombok.*;
import member.domain.CommentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private Long memberId;  // 댓글 작성자의 ID 추가
    private LocalDateTime commentCreatedTime;


    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setBoardId(boardId);
        commentDTO.setMemberId(commentEntity.getMemberEntity().getId());  // 작성자 ID 추가
        return commentDTO;
    }
}
