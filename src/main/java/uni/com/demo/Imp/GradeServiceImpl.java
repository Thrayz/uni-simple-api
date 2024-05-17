package uni.com.demo.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.com.demo.Entity.Grade;
import uni.com.demo.Repository.GradeRepository;
import uni.com.demo.Service.GradeService;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public Grade getGradeById(long id) {
        return gradeRepository.findById(id).orElse(null);
    }

    @Override
    public Grade updateGrade(Grade grade, long id) {
        if (gradeRepository.existsById(id)) {
            grade.setId(id);
            return gradeRepository.save(grade);
        }
        return null;
    }

    @Override
    public void deleteGrade(long id) {
        gradeRepository.deleteById(id);
    }
}
