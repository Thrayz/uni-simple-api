package uni.com.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uni.com.demo.Entity.Teacher;
import uni.com.demo.Service.TeacherService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping()
    public ResponseEntity<Teacher> saveTeacher(@RequestBody Teacher teacher) {
        return new ResponseEntity<>(teacherService.saveTeacher(teacher), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") long teacherId) {
        return new ResponseEntity<>(teacherService.getTeacherById(teacherId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") long id, @RequestBody Teacher teacher) {
        return new ResponseEntity<>(teacherService.updateTeacher(teacher, id), HttpStatus.OK);
    }



    @GetMapping("/list")
    public ModelAndView showTeacherList(Model model) {
        List<Teacher> teacherList = teacherService.getAllTeachers();
        ModelAndView modelAndView = new ModelAndView("teacher-list");
        model.addAttribute("teacherList", teacherList);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long teacherId) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        ModelAndView modelAndView = new ModelAndView("update-teacher");
        modelAndView.addObject("teacher", teacher);
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateTeacher(@PathVariable("id") long teacherId,
                                      @Valid @ModelAttribute("teacher") Teacher teacher,
                                      BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView errorModelAndView = new ModelAndView("update-teacher");
            errorModelAndView.addObject("teacher", teacher);
            return errorModelAndView;
        }
        Teacher updatedTeacher = teacherService.updateTeacher(teacher, teacherId);
        return new ModelAndView("redirect:/api/teachers/list");
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable("id") long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return "redirect:/api/teachers/list";
    }

    @GetMapping("/add")
    public ModelAndView showAddTeacherForm() {
        ModelAndView modelAndView = new ModelAndView("add-teacher");
        modelAndView.addObject("teacher", new Teacher());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addTeacher(@Valid @ModelAttribute("teacher") Teacher teacher, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("add-teacher");
            modelAndView.addObject("error", "An error occurred while adding the teacher.");
            return modelAndView;
        }
        try {
            Teacher addedTeacher = teacherService.saveTeacher(teacher);
            modelAndView.setViewName("redirect:/api/teachers/list");
        } catch (Exception e) {
            modelAndView.setViewName("add-teacher");
            modelAndView.addObject("error", "An error occurred while adding the teacher.");
        }
        return modelAndView;
    }
}

