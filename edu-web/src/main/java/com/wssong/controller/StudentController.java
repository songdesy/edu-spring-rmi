package com.wssong.controller;

import com.wssong.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by WSS on 2017/4/25.
 */
@Controller
public class StudentController {

    @Autowired
    StudentService studentService;

    @RequestMapping("hello")
    public String hello(){
        List<String> list = studentService.getStrList(20);
        for (String s :
                list) {
            System.out.println(s);
        }
        return "hello";
    }
}
