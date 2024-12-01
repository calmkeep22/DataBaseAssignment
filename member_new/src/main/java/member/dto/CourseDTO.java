package member.dto;

import lombok.*;
import member.domain.CourseEntity;
import member.domain.MemberEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseDTO {
    private Long id;
    private String courseName;
    private String courseLocation;
    private LocalTime startTime;
    private LocalTime endTime;
    private String department;

    public static CourseDTO toCourseDTO(CourseEntity courseEntity) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(courseEntity.getId());
        courseDTO.setCourseName(courseEntity.getCourseName());
        courseDTO.setCourseLocation(courseEntity.getCourseLocation());
        courseDTO.setStartTime(courseEntity.getStartTime());
        courseDTO.setEndTime(courseEntity.getEndTime());
        courseDTO.setDepartment(courseEntity.getDepartment());
        return courseDTO;
    }

}
