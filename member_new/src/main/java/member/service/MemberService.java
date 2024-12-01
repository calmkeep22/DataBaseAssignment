package member.service;

import member.domain.CourseEntity;
import member.dto.CourseDTO;
import member.dto.MemberDTO;
import member.domain.MemberEntity;
import member.repository.CourseRepository;
import member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    @Transactional
    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public Long getMemberIdByEmail(String email) {
        return memberRepository.findByMemberEmail(email)
                .map(MemberEntity::getId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + email));
    }

    public MemberDTO login(MemberDTO memberDTO) {
        return memberRepository.findByMemberEmail(memberDTO.getMemberEmail())
                .filter(memberEntity -> memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword()))
                .map(MemberDTO::toMemberDTO)
                .orElse(null); // 로그인 실패 시 null 반환
    }

    public List<MemberDTO> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberDTO::toMemberDTO)
                .collect(Collectors.toList());
    }

    public MemberDTO findById(Long id) {
        return memberRepository.findById(id)
                .map(MemberDTO::toMemberDTO)
                .orElse(null);
    }

    public MemberDTO updateForm(String myEmail) {
        return memberRepository.findByMemberEmail(myEmail)
                .map(MemberDTO::toMemberDTO)
                .orElse(null);
    }

    @Transactional
    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }
    public boolean isEmailDuplicate(String email) {
        return memberRepository.findByMemberEmail(email).isPresent(); // 이메일이 존재하면 true 반환
    }
    /*@Transactional
    public void deleteById(Long id) {

        memberRepository.deleteById(id);
    }*/
    @Transactional
    public void deleteById(Long memberId) {
        // 1. MemberEntity 가져오기
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 2. 모든 CourseEntity와의 관계 해제
        if (member.getCourseEntities() != null) {
            List<CourseEntity> courses = member.getCourseEntities();
            for (int i = 0; i < courses.size(); i++) {
                CourseEntity course = courses.get(i);
                member.removeCourse(course); // 관계 해제 메서드 호출
            }
        }

        // 3. 변경 사항 저장
        memberRepository.save(member); // 관계가 끊어진 상태 저장

        // 4. Member 삭제
        memberRepository.deleteById(memberId);
    }




    public String emailCheck(String memberEmail) {
        boolean exists = memberRepository.findByMemberEmail(memberEmail).isPresent();
        return exists ? "not_available" : "available";
    }

    @Transactional
    public List<CourseDTO> findCoursesByMemberId(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        List<CourseEntity> courseEntities = new ArrayList<>();
        courseEntities = memberEntity.getCourseEntities();

        return courseEntities.stream()
                .map(CourseDTO::toCourseDTO)
                .collect(Collectors.toList());
    }
}
