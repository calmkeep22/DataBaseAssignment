package member.repository;
//추가코드
import org.springframework.transaction.annotation.Transactional;

import member.domain.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits-1 where b.id=:id")
    void minusHits(@Param("id") Long id);
    //추가코드
    @Modifying
    @Query("DELETE FROM BoardEntity b WHERE b.id = :boardId AND b.memberEntity.id = :memberId")
    void deleteByIdAndMemberEntity_Id(Long boardId, Long memberId);
}

