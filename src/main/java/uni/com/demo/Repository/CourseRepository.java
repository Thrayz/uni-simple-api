package uni.com.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.com.demo.Entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
