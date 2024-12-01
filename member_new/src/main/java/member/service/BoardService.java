package member.service;

import member.domain.MemberEntity;
import member.dto.BoardDTO;
import member.domain.BoardEntity;
import member.domain.BoardFileEntity;
import member.repository.BoardFileRepository;
import member.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Inheritance;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    //추가
    private final MemberRepository memberRepository;
    public void save(BoardDTO boardDTO,Long memberId) throws IOException {
        //추가
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        if (boardDTO.getBoardFile().isEmpty()) {
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO,memberEntity);
            boardRepository.save(boardEntity);
        } else {
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFilename = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            String savePath = "C:/file/" +storedFileName;
            File fileDir = new File("C:/file");  // 상대 경로 기준으로 폴더 확인
            if (!fileDir.exists()) {
                fileDir.mkdirs();  // 폴더가 없다면 생성
            }
            boardFile.transferTo(new File(savePath));
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO,memberEntity);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();

            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity);
        }

    }
    @Transactional
    public void minusHits(Long id){boardRepository.minusHits(id);}
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }
    //추가코드
    @Transactional
    public boolean deleteBoard(Long boardId, Long memberId) {
        // 게시글을 가져옴
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);

        if (boardOptional.isPresent()) {
            BoardEntity board = boardOptional.get();

            // 작성자 ID (writer_id)와 요청한 사용자의 ID (memberId)가 동일한지 확인
            if (board.getMemberEntity().getId().equals(memberId)) {
                // 동일하면 삭제
                boardRepository.deleteById(boardId);
                return true;
            }
        }
        // 동일하지 않으면 삭제할 수 없음
        return false;
    }

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(),
                board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}














