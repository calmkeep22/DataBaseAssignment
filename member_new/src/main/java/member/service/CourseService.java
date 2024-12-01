package member.service;

import member.dto.CourseDTO;
import member.domain.CourseEntity;
import member.domain.MemberEntity;
import member.dto.MemberDTO;
import member.repository.CourseRepository;
import member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(CourseDTO courseDTO, Long memberId) {
        // 해당 memberId에 해당하는 MemberEntity 조회
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // CourseEntity 생성 및 필드 설정
        CourseEntity courseEntity = CourseEntity.toCourseEntity(courseDTO);

        // MemberEntity와의 관계 설정
        courseEntity.addMember(memberEntity);

        // CourseEntity 저장
        courseRepository.save(courseEntity);
    }
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseDTO::toCourseDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public void enrollCourse(Long memberId, Long courseId) {
        // 1. MemberEntity 가져오기
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 2. CourseEntity 가져오기
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // 3. 관계 설정
        member.addCourse(course);
        memberRepository.save(member); // 변경 사항 저장 (Cascading 설정 필요)
    }



    @Transactional
    public void addCourse(Long memberId, String courseName, String courseLocation,
                          LocalTime startTime, LocalTime endTime, String department) {
        // 해당 memberId에 해당하는 MemberEntity 조회
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 새로운 CourseEntity 생성 및 필드 설정
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCourseName(courseName);
        courseEntity.setCourseLocation(courseLocation);
        courseEntity.setStartTime(startTime);
        courseEntity.setEndTime(endTime);
        courseEntity.setDepartment(department);

        // MemberEntity와의 관계 설정
        courseEntity.addMember(memberEntity);

        // CourseEntity 저장
        courseRepository.save(courseEntity);
    }

    @Transactional
    public void deleteEnrollment(Long memberId, Long courseId) {
        // 1. MemberEntity 가져오기
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 2. CourseEntity 가져오기
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // 3. 관계 해제
        member.removeCourse(course);

        // 4. 변경 사항 저장
        memberRepository.save(member); // 관계가 끊어진 상태를 저장
    }
}
