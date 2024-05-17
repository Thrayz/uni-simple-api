package uni.com.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.com.demo.Entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
