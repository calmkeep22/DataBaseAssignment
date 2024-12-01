package member.controller;

import member.domain.BoardEntity;
import member.dto.CommentDTO;
import member.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    /*@PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        System.out.println("commentDTO = " + commentDTO);
        Long saveResult = commentService.save(commentDTO);
        if (saveResult != null) {
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }*/
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO, HttpSession session) {
        // 세션에서 memberId 가져오기
        Long memberId = (Long) session.getAttribute("memberId");

        // 로그인되지 않은 사용자의 경우
        if (memberId == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED); // 로그인 필요 메시지
        }

        // 댓글 저장 로직을 서비스에서 처리하도록 호출
        Long savedCommentId = commentService.save(commentDTO, memberId);

        if (savedCommentId != null) {
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);  // 댓글 리스트 반환
        } else {
            return new ResponseEntity<>("댓글 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);  // 댓글 저장 실패
        }
    }

}