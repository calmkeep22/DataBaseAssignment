package member.service;

import member.domain.MemberEntity;
import member.dto.CommentDTO;
import member.domain.BoardEntity;
import member.domain.CommentEntity;
import member.repository.BoardRepository;
import member.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import member.repository.CourseRepository;
import member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /*public Long save(CommentDTO commentDTO) {

        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            MemberEntity memberEntity = optionalBoardEntity.get().getMemberEntity();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity, memberEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }*/
    public Long save(CommentDTO commentDTO, Long memberId) {
        // 로그인된 사용자가 존재하는지 확인
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));  // 회원 정보가 없으면 예외 발생

        // 댓글을 저장할 때, 댓글 작성자는 로그인된 사용자와 연결되도록 설정
        // 게시글 ID는 commentDTO에 포함되어 있고, 해당 게시글은 반드시 존재한다고 가정
        BoardEntity boardEntity = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));  // 게시글이 없으면 예외 발생

        // 댓글 저장
        CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity, memberEntity);
        commentRepository.save(commentEntity);

        // 저장된 댓글의 ID 반환
        return commentEntity.getId();
    }



    public List<CommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
