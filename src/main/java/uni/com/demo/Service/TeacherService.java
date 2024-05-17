package uni.com.demo.Service;

import uni.com.demo.Entity.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher saveTeacher(Teacher teacher);
    List<Teacher> getAllTeachers();
    Teacher getTeacherById(long id);
    Teacher updateTeacher(Teacher teacher, long id);
    void deleteTeacher(long id);
}
