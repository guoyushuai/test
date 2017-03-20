package com.gys.controller;

import com.gys.pojo.Student;
import com.gys.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String list(Model model) {

        List<Student> studentList = studentService.findAll();
        model.addAttribute("studentList",studentList);
        return "student/list";
    }

    @GetMapping("/view/{stuid}")
    public String view(@PathVariable Integer stuid, Model model) {

        Student student = studentService.findById(stuid);
        model.addAttribute("student",student);
        return "student/view";
    }

}
