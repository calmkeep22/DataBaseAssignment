package member.domain;

import lombok.*;
import member.dto.CourseDTO;
import member.domain.MemberEntity;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "course_table")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String courseName;

    @Column(nullable = false)
    private String courseLocation;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(length = 50)
    private String department;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")

    )
    private List<MemberEntity> memberEntities = new ArrayList<>();

    public void addMember(MemberEntity memberEntity) {
        if (!this.memberEntities.contains(memberEntity)) {
            this.memberEntities.add(memberEntity);
            if (!memberEntity.getCourseEntities().contains(this)) {
                memberEntity.getCourseEntities().add(this);
            }
        }
    }

    public static CourseEntity toCourseEntity(CourseDTO courseDTO) {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCourseName(courseDTO.getCourseName());
        courseEntity.setCourseLocation(courseDTO.getCourseLocation());
        courseEntity.setStartTime(courseDTO.getStartTime());
        courseEntity.setEndTime(courseDTO.getEndTime());
        courseEntity.setDepartment(courseDTO.getDepartment());
        return courseEntity;
    }
}


