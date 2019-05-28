package com.grademanagement.controller;

import com.grademanagement.pojo.Curriculum;
import com.grademanagement.pojo.User;
import com.grademanagement.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 该类提供对数据字典的更新操作
 */
@Controller
@RequestMapping("/settings")
public class Settings {
    @Autowired
    private CurriculumService curriculumService;

    @RequestMapping(value = "/{path}")
    public String pathParam(@PathVariable String path) {
        return path;
    }

    @RequestMapping("getCurriculumInfo")
    @ResponseBody
    public List<Curriculum> getCurriculumInfo() {
        List<Curriculum> curriculums = curriculumService.findAll();
        return curriculums;
    }

    @RequestMapping("delCourse")
    @ResponseBody
    public Boolean delCourse(String id) {
        int i = curriculumService.delCourse(id);
        return i > 0 ? true : false;
    }

    @RequestMapping("insert")
    @ResponseBody
    public boolean  insert(Curriculum curriculum) {
        System.out.println(curriculum.getName() + curriculum.gettName());
        boolean isSuccess = curriculumService.insertCurriculum(curriculum);
        return isSuccess;
    }

    @RequestMapping("getTeachers")
    @ResponseBody
    public List getTeachers() {
        List<User> teachers = curriculumService.getTeachers();
        return teachers;
    }
}
