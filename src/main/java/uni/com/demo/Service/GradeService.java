package uni.com.demo.Service;

import uni.com.demo.Entity.Grade;

import java.util.List;

public interface GradeService {
    Grade saveGrade(Grade grade);
    List<Grade> getAllGrades();
    Grade getGradeById(long id);
    Grade updateGrade(Grade grade, long id);
    void deleteGrade(long id);
     List<Grade> getGradesForStudent(long studentId);
}
