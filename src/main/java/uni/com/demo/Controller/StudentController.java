package uni.com.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uni.com.demo.Entity.Attendance;
import uni.com.demo.Entity.Grade;
import uni.com.demo.Entity.Student;
import uni.com.demo.Service.AttendanceService;
import uni.com.demo.Service.GradeService;
import uni.com.demo.Service.StudentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final GradeService gradeService;
    private final AttendanceService attendanceService;

    public StudentController(StudentService studentService, GradeService gradeService, AttendanceService attendanceService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.attendanceService = attendanceService;
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


    @GetMapping("/students/{studentId}/stats")
    public ModelAndView getStudentStats(@PathVariable long studentId) {
        ModelAndView modelAndView = new ModelAndView("student-stats");

        List<Grade> grades = gradeService.getGradesForStudent(studentId);
        List<Attendance> attendances = attendanceService.getAttendancesForStudent(studentId);


        double totalGrade = 0.0;
        for (Grade grade : grades) {
            totalGrade += grade.getValue();
        }
        double averageGrade = grades.isEmpty() ? 0.0 : totalGrade / grades.size();


        int totalAttendances = attendances.size();
        int totalPresent = (int) attendances.stream().filter(Attendance::isPresent).count();
        double attendancePercentage = totalAttendances == 0 ? 0.0 : (double) totalPresent / totalAttendances * 100;


        modelAndView.addObject("averageGrade", averageGrade);
        modelAndView.addObject("attendancePercentage", attendancePercentage);

        return modelAndView;
    }

}
