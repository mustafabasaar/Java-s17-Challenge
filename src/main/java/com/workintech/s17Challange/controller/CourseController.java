package com.workintech.s17Challange.controller;

import com.workintech.s17Challange.entity.ApiResponse;
import com.workintech.s17Challange.entity.Course;
import com.workintech.s17Challange.entity.CourseGpa;
import com.workintech.s17Challange.exceptions.ApiException;
import com.workintech.s17Challange.validation.CourseValidation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/courses")
public class CourseController {
    List<Course> courses;
    private final CourseGpa lowCourseGpa;
    private final CourseGpa mediumCourseGpa;
    private final CourseGpa highCourseGpa;

    public CourseController(@Qualifier("lowCourseGpa") CourseGpa lowCourseGpa,@Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa, @Qualifier("highCourseGpa") CourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @PostConstruct
    public void init(){
        courses=new ArrayList<>();
    }
    @GetMapping
    public List<Course> findCourse(){
        return this.courses;
    }
   @GetMapping("/{name}")
    public Course findWithName(@PathVariable("name") String name){
       CourseValidation.checkName(name);

        return  courses.stream()
                .filter(course -> course.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(()-> new ApiException("course not found with name: "+ name, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity <ApiResponse>saveCourse(@RequestBody Course course){
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.checkName(course.getName());
        courses.add(course);
        Integer totalGpa = getTotalGpa(course);
        ApiResponse apiResponse = new ApiResponse(course,totalGpa);
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }
    private Integer getTotalGpa(Course course) {
        if (course.getCredit() <= 2) {
            return course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa();
        } else if (course.getCredit() == 3) {
            return course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa();
        } else {
            return course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa();
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable("id") Integer id,@RequestBody Course course){
        CourseValidation.checkId(id);
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.checkName(course.getName());
        Course existingCourse = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException("course not found with id: ", HttpStatus.BAD_REQUEST));
        int indexOfExistingCourse = courses.indexOf(existingCourse);
        course.setId(id);
        courses.set(indexOfExistingCourse,course);
        Integer totalGpa = getTotalGpa(course);
        ApiResponse apiResponse = new ApiResponse(courses.get(indexOfExistingCourse),totalGpa);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id){
        Course existingCourse = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException("course not found with id: ", HttpStatus.BAD_REQUEST));
        courses.remove(existingCourse);
    }

}
