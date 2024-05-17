package uni.com.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uni.com.demo.Entity.Course;
import uni.com.demo.Service.CourseService;
import uni.com.demo.Service.TeacherService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping()
    public ResponseEntity<Course> saveCourse(@RequestBody Course course) {
        return new ResponseEntity<>(courseService.saveCourse(course), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long courseId) {
        return new ResponseEntity<>(courseService.getCourseById(courseId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") long id, @RequestBody Course course) {
        return new ResponseEntity<>(courseService.updateCourse(course, id), HttpStatus.OK);
    }



    @GetMapping("/list")
    public ModelAndView showCourseList(Model model) {
        List<Course> courseList = courseService.getAllCourses();
        ModelAndView modelAndView = new ModelAndView("courseList");
        model.addAttribute("courseList", courseList);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long courseId) {
        Course course = courseService.getCourseById(courseId);
        ModelAndView modelAndView = new ModelAndView("update-course");
        modelAndView.addObject("course", course);
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateCourse(@PathVariable("id") long courseId,
                                     @Valid @ModelAttribute("course") Course course,
                                     BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView errorModelAndView = new ModelAndView("update-course");
            errorModelAndView.addObject("course", course);
            return errorModelAndView;
        }
        Course updatedCourse = courseService.updateCourse(course, courseId);
        return new ModelAndView("redirect:/api/courses/list");
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") long courseId) {
        courseService.deleteCourse(courseId);
        return "redirect:/api/courses/list";
    }

    @GetMapping("/add")
    public ModelAndView showAddCourseForm() {
        ModelAndView modelAndView = new ModelAndView("add-course");
        modelAndView.addObject("course", new Course());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addCourse(@Valid @ModelAttribute("course") Course course, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("add-course");
            modelAndView.addObject("error", "An error occurred while adding the course.");
            return modelAndView;
        }
        try {
            Course addedCourse = courseService.saveCourse(course);
            modelAndView.setViewName("redirect:/api/courses/list");
        } catch (Exception e) {
            modelAndView.setViewName("add-course");
            modelAndView.addObject("error", "An error occurred while adding the course.");
        }
        return modelAndView;
    }
}
