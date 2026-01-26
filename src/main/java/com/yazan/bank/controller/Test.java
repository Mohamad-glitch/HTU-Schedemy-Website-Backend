package com.yazan.bank.controller;

import com.yazan.bank.model.Courses;
import com.yazan.bank.model.Department;
import com.yazan.bank.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class Test {
    @Autowired
    private final CourseService courseService;

    public Test(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public int getAllCourses() {
        return 0;
    }

    @GetMapping("/one")
    public int g(){
        return 1;
    }

    @GetMapping("/test")
    public String testDeployment(){
        return "new update";
    }
}