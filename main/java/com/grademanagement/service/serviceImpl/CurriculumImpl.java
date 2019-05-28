package com.grademanagement.service.serviceImpl;

import com.grademanagement.mapper.AchievementMapper;
import com.grademanagement.mapper.CurriculumMapper;
import com.grademanagement.mapper.UserMapper;
import com.grademanagement.mapper.UserRoleMapper;
import com.grademanagement.pojo.*;
import com.grademanagement.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CurriculumImpl implements CurriculumService {
    @Autowired
    private CurriculumMapper curriculumMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AchievementMapper achievementMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public List<User> getCurriculumInfo() {


        return curriculumMapper.getCurriculumInfo();
    }

    @Override
    public List<Curriculum> findAll() {
        return curriculumMapper.selectByExample(new CurriculumExample());
    }

    @Override
    public List<ExcelDto> downLoad(Map map) {

        return curriculumMapper.getCurriculumInfo2(map);
    }

    public Long getCurUserId(User curUser) {
        //通过当前的用户获取到它所对应的数据库中的id
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(curUser.getName());
        List<User> users = userMapper.selectByExample(userExample);
        Long id = users.get(0).getId();
        //根据用户id获取角色的id，如果校色的id为2的话，直接返回id，否则返回空值
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUidEqualTo(id);
        List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
        //当前用户是老师
        if (userRoleKeys.get(0).getRid() == 2) {
            return id;
        }
        return null;
    }

    /*
    导入成绩功能：
    1.通过用户的姓名获取到用户的id，如果没有此用户：则放弃这条数据
    2.具有此用户，获取到此用户的课程信息，如果已经存在此条课程的数据则使用本条数据将上条数据覆盖掉
     */
    @Override
    @Transactional
    public String insertMulitPart(List<ExcelDto> excelDtoList) {

        User user = null;
        Achievement achievement = null;
        int score = 0;
        Long cid = null;
        Long id = null;
        for (ExcelDto e :
                excelDtoList) {
            //添加用户
            user = new User();
            user.setName(e.getName());//表中的用户名
//            user.setPassword(MD5.toPassWord("1111"));//密码默认设置为1111
//            userMapper.insertSelective(user);//插入进入数据库之中


            //添加achievement中的关系数据
            //1.获取刚才插入到user中的user表的user的id，通过姓名
            id = getUserID(user);
            if (id != null) {

            }
            //给学生添加统一默认权限
//            UserRoleKey userRoleKey = new UserRoleKey();
//            userRoleKey.setUid(id);
//            userRoleKey.setRid(3l);
//            userRoleMapper.insertSelective(userRoleKey);
            //获取课程的id
            cid = getCourseID(e.getCname());
            AchievementExample achievementExample = new AchievementExample();
            achievementExample.createCriteria().andCidEqualTo(cid).andUidEqualTo(id);
            List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
            if(achievements.size()>0){//证明已经存在此条成绩信息，对原有记录进行修改
                Achievement record=new Achievement();
                record.setScore(e.getScore());
                record.setUid(id);
                record.setCid(cid);

                AchievementExample example=new AchievementExample();
                example.createCriteria().andUidEqualTo(id).andCidEqualTo(cid);
                achievementMapper.updateByExampleSelective(record,example);

            }else {//直接添加进入数据库表中
                //获取表单传上来的成绩
                score = e.getScore();
                //将id,cid,score封装进入achievement对象中
                achievement = new Achievement();
                achievement.setCid(cid);
                achievement.setUid(id);
                achievement.setScore(score);
                achievementMapper.insertSelective(achievement);
            }
        }
        return "添加成功";
    }

    @Override
    public ExcelDto getModifyCourseInfo(Long id, Long cid) {
        ExcelDto excelDto = new ExcelDto();
        excelDto.setCid(cid.intValue());
        excelDto.setId(id.intValue());
        return curriculumMapper.getModifyCourseInfo(excelDto);
    }

    @Override
    public int selectDataCount(Map hashMap) {

        Integer i = curriculumMapper.getCurriculumInfoCount(hashMap);
        return i;
    }

    /*
    删除课程信息并且将成绩表中关于这门课程的成绩也进行删除
     */
    @Override
    @Transactional
    public int delCourse(String id) {
        AchievementExample example = new AchievementExample();
        AchievementExample.Criteria criteria = example.createCriteria();
        criteria.andCidEqualTo(Long.valueOf(id));
        int i1 = achievementMapper.deleteByExample(example);
        int i = curriculumMapper.deleteByPrimaryKey(Long.valueOf(id));
        return i;
    }

    /*
    添加课程信息
     */
    @Override
    public boolean insertCurriculum(Curriculum curriculum) {
        //插入课程之前首先检测是否具有此门课程的信息
        CurriculumExample example = new CurriculumExample();
        example.createCriteria().andNameEqualTo(curriculum.getName()).andTNameEqualTo(curriculum.gettName());
        List<Curriculum> curriculumList = curriculumMapper.selectByExample(example);
        if (curriculumList.size() == 0) {
            //List==0证明没有此条信息，直接插入，不然返回无效操作
            //获取到老师的id直接插入进课程包中的id中
            UserExample userExample=new UserExample();
            userExample.createCriteria().andNameEqualTo(curriculum.getName());
            List<User> users = userMapper.selectByExample(userExample);
            curriculum.setId(users.get(0).getId());
            System.err.println("没有此课程的信息");
            int i = curriculumMapper.insertSelective(curriculum);
            return true;
        } else {
            return false;
        }
    }

    @Override
    //获取所有的老师的信息
    public List<User> getTeachers() {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andRidEqualTo(2l);
        List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);

        List<User> teachers = new ArrayList<User>();
        userRoleKeys.forEach(userRoleKey -> {
            //通过查询道德user_role表查询user表的用户的信息，添加进入集合中
            User user = userMapper.selectByPrimaryKey(userRoleKey.getUid());
            teachers.add(user);
        });
        return teachers;
    }

    private Long getCourseID(String cname) {
        CurriculumExample curriculumExample = new CurriculumExample();
        CurriculumExample.Criteria criteria = curriculumExample.createCriteria();
        criteria.andNameEqualTo(cname);
        List<Curriculum> curriculumList = curriculumMapper.selectByExample(curriculumExample);
        if (curriculumList.size() > 0) {
            return curriculumList.get(0).getId();
        }
        return null;
    }

    private Long getUserID(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNameEqualTo(user.getName());
        //执行查询获取用户的id
        List<User> users = userMapper.selectByExample(userExample);
        Long id = null;
        if (users.size() > 0) {
            //限制：不能有重名的人
            if (users.get(0) == null) return null;
            return id = users.get(0).getId();
        }
        return id;
    }

}
