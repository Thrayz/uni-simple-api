package uni.com.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uni.com.demo.Entity.Grade;
import uni.com.demo.Service.CourseService;
import uni.com.demo.Service.GradeService;
import uni.com.demo.Service.StudentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping()
    public ResponseEntity<Grade> saveGrade(@RequestBody Grade grade) {
        return new ResponseEntity<>(gradeService.saveGrade(grade), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable("id") long gradeId) {
        return new ResponseEntity<>(gradeService.getGradeById(gradeId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable("id") long id, @RequestBody Grade grade) {
        return new ResponseEntity<>(gradeService.updateGrade(grade, id), HttpStatus.OK);
    }



    @GetMapping("/list")
    public ModelAndView showGradeList(Model model) {
        List<Grade> gradeList = gradeService.getAllGrades();
        ModelAndView modelAndView = new ModelAndView("gradeList");
        model.addAttribute("gradeList", gradeList);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long gradeId) {
        Grade grade = gradeService.getGradeById(gradeId);
        ModelAndView modelAndView = new ModelAndView("update-grade");
        modelAndView.addObject("grade", grade);
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateGrade(@PathVariable("id") long gradeId,
                                    @Valid @ModelAttribute("grade") Grade grade,
                                    BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView errorModelAndView = new ModelAndView("update-grade");
            errorModelAndView.addObject("grade", grade);
            return errorModelAndView;
        }
        Grade updatedGrade = gradeService.updateGrade(grade, gradeId);
        return new ModelAndView("redirect:/api/grades/list");
    }

    @GetMapping("/delete/{id}")
    public String deleteGrade(@PathVariable("id") long gradeId) {
        gradeService.deleteGrade(gradeId);
        return "redirect:/api/grades/list";
    }

    @GetMapping("/add")
    public ModelAndView showAddGradeForm() {
        ModelAndView modelAndView = new ModelAndView("add-grade");
        modelAndView.addObject("grade", new Grade());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addGrade(@Valid @ModelAttribute("grade") Grade grade, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("add-grade");
            modelAndView.addObject("error", "An error occurred while adding the grade.");
            return modelAndView;
        }
        try {
            Grade addedGrade = gradeService.saveGrade(grade);
            modelAndView.setViewName("redirect:/api/grades/list");
        } catch (Exception e) {
            modelAndView.setViewName("add-grade");
            modelAndView.addObject("error", "An error occurred while adding the grade.");
        }
        return modelAndView;
    }
}
