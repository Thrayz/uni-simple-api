package uni.com.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uni.com.demo.Entity.Student;
import uni.com.demo.Service.StudentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping()
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        return new ResponseEntity<>(studentService.saveStudent(student), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") long studentId) {
        return new ResponseEntity<>(studentService.getStudentById(studentId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") long id, @RequestBody Student student) {
        return new ResponseEntity<>(studentService.updateStudent(student, id), HttpStatus.OK);
    }



    @GetMapping("/list")
    public ModelAndView showStudentList(Model model) {
        List<Student> studentList = studentService.getAllStudents();
        System.out.println(studentList);
        ModelAndView modelAndView = new ModelAndView("student-list");
        model.addAttribute("studentList", studentList);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long studentId) {
        Student student = studentService.getStudentById(studentId);
        ModelAndView modelAndView = new ModelAndView("update-student");
        modelAndView.addObject("student", student);
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateStudent(@PathVariable("id") long studentId,
                                      @Valid @ModelAttribute("student") Student student,
                                      BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView errorModelAndView = new ModelAndView("update-student");
            errorModelAndView.addObject("student", student);
            return errorModelAndView;
        }
        Student updatedStudent = studentService.updateStudent(student, studentId);
        return new ModelAndView("redirect:/api/students/list");
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") long studentId) {
        studentService.deleteStudent(studentId);
        return "redirect:/api/students/list";
    }

    @GetMapping("/add")
    public ModelAndView showAddStudentForm() {
        ModelAndView modelAndView = new ModelAndView("add-student");
        modelAndView.addObject("student", new Student());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addStudent(@Valid @ModelAttribute("student") Student student, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("add-student");
            modelAndView.addObject("error", "An error occurred while adding the student.");
            return modelAndView;
        }
        try {
            Student addedStudent = studentService.saveStudent(student);
            modelAndView.setViewName("redirect:/api/students/list");
        } catch (Exception e) {
            modelAndView.setViewName("add-student");
            modelAndView.addObject("error", "An error occurred while adding the student.");
        }
        return modelAndView;
    }
}
