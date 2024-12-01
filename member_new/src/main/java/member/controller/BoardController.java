package member.controller;
import member.repository.MemberRepository;
import member.domain.MemberEntity;
import member.dto.BoardDTO;
import member.dto.CommentDTO;
import member.dto.MemberDTO;
import member.service.BoardService;
import member.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @GetMapping("/save")
    public String saveForm() {
        return "boardsave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO, Model model, /*추가*/HttpSession session) throws IOException {
        //추가
        Long memberId = (Long) session.getAttribute("memberId"); // memberId는 세션에 저장된 로그인 사용자 정보
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        if (boardDTO.getBoardTitle() == null || boardDTO.getBoardTitle().isEmpty() ||
                boardDTO.getBoardContents() == null || boardDTO.getBoardContents().isEmpty()||

                boardDTO.getBoardWriter() == null || boardDTO.getBoardWriter().isEmpty()) {
            model.addAttribute("errorMessage", "제목과 내용을 모두 입력해주세요.");
            return "boardsave";
        }

        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO, memberId);
        return "redirect:/board/paging";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());

        return "boarddetail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // 세션에서 로그인한 사용자의 memberId 가져오기
        Long memberId = (Long) session.getAttribute("memberId");
        System.out.println(memberId);
        if (memberId == null) {
            // 로그인 안 된 경우 처리 (예: 로그인 페이지로 리다이렉트)
            return "login";
            //return "redirect:/login";
        }

        // 게시글 삭제 시, 사용자가 본인이 작성한 글만 삭제 가능
        boolean isDeleted = boardService.deleteBoard(id, memberId);

        if (isDeleted) {
            return "redirect:/board/paging";  // 삭제 성공 시 페이지로 리다이렉트
        } else {
            boardService.minusHits(id);
            redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 삭제할 수 있습니다.");
            return "redirect:/board/{id}";
        }
    }
    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
//        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";

    }

}










