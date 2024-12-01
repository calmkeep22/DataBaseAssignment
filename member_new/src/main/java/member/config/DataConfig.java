package member.config;

import member.domain.CourseEntity;
import member.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataConfig {

    // CommandLineRunner는 Spring Boot 애플리케이션 시작 시 자동으로 실행됨
    @Bean
    public CommandLineRunner demo(CourseRepository courseRepository) {
        return args -> {
            // 여러 CourseEntity 객체 생성
            CourseEntity course1 = new CourseEntity();
            course1.setCourseName("Computer Science 101");
            course1.setCourseLocation("Room 201");
            course1.setStartTime(LocalTime.of(9, 0));  // 9:00 AM
            course1.setEndTime(LocalTime.of(11, 0));  // 11:00 AM
            course1.setDepartment("Computer Science");

            CourseEntity course2 = new CourseEntity();
            course2.setCourseName("Mathematics 101");
            course2.setCourseLocation("Room 202");
            course2.setStartTime(LocalTime.of(10, 0)); // 10:00 AM
            course2.setEndTime(LocalTime.of(12, 0));  // 12:00 PM
            course2.setDepartment("Mathematics");

            CourseEntity course3 = new CourseEntity();
            course3.setCourseName("Physics 101");
            course3.setCourseLocation("Room 203");
            course3.setStartTime(LocalTime.of(1, 0));  // 1:00 PM
            course3.setEndTime(LocalTime.of(3, 0));  // 3:00 PM
            course3.setDepartment("Physics");

            CourseEntity course4 = new CourseEntity();
            course4.setCourseName("Biology 101");
            course4.setCourseLocation("Room 204");
            course4.setStartTime(LocalTime.of(11, 0));  // 11:00 AM
            course4.setEndTime(LocalTime.of(1, 0));  // 1:00 PM
            course4.setDepartment("Biology");

            CourseEntity course5 = new CourseEntity();
            course5.setCourseName("Chemistry 101");
            course5.setCourseLocation("Room 205");
            course5.setStartTime(LocalTime.of(2, 0));  // 2:00 PM
            course5.setEndTime(LocalTime.of(4, 0));  // 4:00 PM
            course5.setDepartment("Chemistry");

            CourseEntity course6 = new CourseEntity();
            course6.setCourseName("English 101");
            course6.setCourseLocation("Room 206");
            course6.setStartTime(LocalTime.of(8, 0));  // 8:00 AM
            course6.setEndTime(LocalTime.of(10, 0));  // 10:00 AM
            course6.setDepartment("English");

            CourseEntity course7 = new CourseEntity();
            course7.setCourseName("History 101");
            course7.setCourseLocation("Room 207");
            course7.setStartTime(LocalTime.of(3, 0));  // 3:00 PM
            course7.setEndTime(LocalTime.of(5, 0));  // 5:00 PM
            course7.setDepartment("History");

            CourseEntity course8 = new CourseEntity();
            course8.setCourseName("Philosophy 101");
            course8.setCourseLocation("Room 208");
            course8.setStartTime(LocalTime.of(5, 0));  // 5:00 PM
            course8.setEndTime(LocalTime.of(7, 0));  // 7:00 PM
            course8.setDepartment("Philosophy");

            // 여러 객체를 리스트로 묶어서 한번에 저장
            List<CourseEntity> courses = Arrays.asList(course1, course2, course3, course4, course5, course6, course7, course8);
            for (CourseEntity newCourse : courses) {
                // courseName으로 중복 확인
                boolean exists = courseRepository.existsByCourseName(newCourse.getCourseName());

                if (!exists) {
                    courseRepository.save(newCourse); // ID는 자동 생성됨
                }
            }

        };
    }
}