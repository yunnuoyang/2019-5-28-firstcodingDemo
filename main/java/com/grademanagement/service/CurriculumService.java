package com.grademanagement.service;

import com.grademanagement.pojo.Curriculum;
import com.grademanagement.pojo.ExcelDto;
import com.grademanagement.pojo.User;

import java.util.List;
import java.util.Map;

public interface CurriculumService {
    List<User> getCurriculumInfo();

    List<Curriculum> findAll();

    List<ExcelDto> downLoad(Map map);
    public Long getCurUserId( User curUser);
    String insertMulitPart(List<ExcelDto> excelDtoList);

    /**
     * 查询此用户的此门课程的信息
     * @param id 用户的id
     * @param cid 课程的id
     * @return
     */
    ExcelDto getModifyCourseInfo(Long id, Long cid);

    int selectDataCount(Map hashMap);

    int delCourse(String id);

    boolean insertCurriculum(Curriculum curriculum);

    List<User> getTeachers();
}
