package uni.com.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uni.com.demo.Entity.Attendance;
import uni.com.demo.Service.AttendanceService;
import uni.com.demo.Service.CourseService;
import uni.com.demo.Service.StudentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final CourseService courseService;

    public AttendanceController(AttendanceService attendanceService, StudentService studentService, CourseService courseService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @PostMapping()
    public ResponseEntity<Attendance> saveAttendance(@RequestBody Attendance attendance) {
        return new ResponseEntity<>(attendanceService.saveAttendance(attendance), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Attendance> getAllAttendances() {
        return attendanceService.getAllAttendances();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable("id") long attendanceId) {
        return new ResponseEntity<>(attendanceService.getAttendanceById(attendanceId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable("id") long id, @RequestBody Attendance attendance) {
        return new ResponseEntity<>(attendanceService.updateAttendance(attendance, id), HttpStatus.OK);
    }



    @GetMapping("/list")
    public ModelAndView showAttendanceList(Model model) {
        List<Attendance> attendanceList = attendanceService.getAllAttendances();
        ModelAndView modelAndView = new ModelAndView("attendanceList");
        model.addAttribute("attendanceList", attendanceList);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long attendanceId) {
        Attendance attendance = attendanceService.getAttendanceById(attendanceId);
        ModelAndView modelAndView = new ModelAndView("update-attendance");
        modelAndView.addObject("attendance", attendance);
        modelAndView.addObject("students", studentService.getAllStudents());
        modelAndView.addObject("courses", courseService.getAllCourses());
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateAttendance(@PathVariable("id") long attendanceId,
                                         @Valid @ModelAttribute("attendance") Attendance attendance,
                                         BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView errorModelAndView = new ModelAndView("update-attendance");
            errorModelAndView.addObject("attendance", attendance);
            errorModelAndView.addObject("students", studentService.getAllStudents());
            errorModelAndView.addObject("courses", courseService.getAllCourses());
            return errorModelAndView;
        }
        Attendance updatedAttendance = attendanceService.updateAttendance(attendance, attendanceId);
        return new ModelAndView("redirect:/api/attendances/list");
    }

    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable("id") long attendanceId) {
        attendanceService.deleteAttendance(attendanceId);
        return "redirect:/api/attendances/list";
    }

    @GetMapping("/add")
    public ModelAndView showAddAttendanceForm() {
        ModelAndView modelAndView = new ModelAndView("add-attendance");
        modelAndView.addObject("attendance", new Attendance());
        modelAndView.addObject("students", studentService.getAllStudents());
        modelAndView.addObject("courses", courseService.getAllCourses());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addAttendance(@Valid @ModelAttribute("attendance") Attendance attendance, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("add-attendance");
            modelAndView.addObject("error", "An error occurred while adding the attendance.");
            modelAndView.addObject("students", studentService.getAllStudents());
            modelAndView.addObject("courses", courseService.getAllCourses());
            return modelAndView;
        }
        try {
            Attendance addedAttendance = attendanceService.saveAttendance(attendance);
            modelAndView.setViewName("redirect:/api/attendances/list");
        } catch (Exception e) {
            modelAndView.setViewName("add-attendance");
            modelAndView.addObject("error", "An error occurred while adding the attendance.");
            modelAndView.addObject("students", studentService.getAllStudents());
            modelAndView.addObject("courses", courseService.getAllCourses());
        }
        return modelAndView;
    }
}
