package member.controller;

import member.dto.CourseDTO;
import member.dto.MemberDTO;
import member.service.CourseService;
import lombok.RequiredArgsConstructor;
import member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import member.dto.EnrollRequest;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final MemberService memberService;
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollCourse(@RequestBody EnrollRequest request) {
        try {
            courseService.enrollCourse(request.getMemberId(), request.getCourseId());
            return ResponseEntity.ok("Course successfully enrolled");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/student/{memberId}")
    public String getCoursesByStudent(@PathVariable Long memberId, Model model) {
        try {
            if (memberId == null) {
                model.addAttribute("error", "유효하지 않은 요청입니다. 메인 페이지로 이동합니다.");
                return "main";
            }

            List<CourseDTO> courseDTOList = memberService.findCoursesByMemberId(memberId);
            List<CourseDTO> allCourses = courseService.getAllCourses();
            MemberDTO memberDTO = memberService.findById(memberId);
            model.addAttribute("member", memberDTO);
            model.addAttribute("courses", courseDTOList);
            model.addAttribute("allCourses", allCourses);
            return "timetable";
        } catch (Exception e) {
            model.addAttribute("error", "요청 처리 중 문제가 발생했습니다. 메인 페이지로 이동합니다.");
            return "main";
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEnrollment(@RequestBody EnrollRequest request) {
        try {
            courseService.deleteEnrollment(request.getMemberId(), request.getCourseId());
            return ResponseEntity.ok("Enrollment deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete enrollment");
        }
    }
}
