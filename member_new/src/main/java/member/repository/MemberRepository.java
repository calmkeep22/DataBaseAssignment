package member.repository;

import member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    //Optional<MemberEntity> findById(Long memberId);
}
