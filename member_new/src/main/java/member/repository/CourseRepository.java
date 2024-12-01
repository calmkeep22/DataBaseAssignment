package member.repository;

import member.domain.CourseEntity;
import member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findById(Long Id);
    boolean existsByCourseName(String courseName);
}

