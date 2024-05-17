package uni.com.demo.Service;

import uni.com.demo.Entity.Course;

import java.util.List;

public interface CourseService {
    Course saveCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(long id);
    Course updateCourse(Course course, long id);
    void deleteCourse(long id);
}


