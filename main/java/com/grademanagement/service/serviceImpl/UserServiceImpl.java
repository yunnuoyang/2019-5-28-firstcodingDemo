package com.grademanagement.service.serviceImpl;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.grademanagement.mapper.RoleMapper;
import com.grademanagement.mapper.UserMapper;
import com.grademanagement.mapper.UserRoleMapper;
import com.grademanagement.pojo.*;
import com.grademanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    /*
    根据用户名，密码查询用户
     */
    @Override
    public User login(User user) {

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNameEqualTo(user.getName());
        criteria.andPasswordEqualTo(user.getPassword());
        List<User> userList = userMapper.selectByExample(userExample);
        return userList.get(0);
    }
    /*
    查询所有用户
     */
    @Override
    public List<User> getUserList() {
//        UserExample example=new UserExample();
//        example.createCriteria().and
//        userMapper.selectByExample();
//        UserExample userExample = new UserExample();
//        userExample.createCriteria().andNameNotIn();
        return userMapper.selectByExample(new UserExample());
    }
/*
根据用户id进行删除用户
 */
    @Override
    public boolean delUser(String id) {
        UserRoleExample userRoleExample=new UserRoleExample();
        UserRoleExample.Criteria criteria = userRoleExample.createCriteria();
        criteria.andUidEqualTo(Long.valueOf(id));
        userRoleMapper.deleteByExample(userRoleExample);
        return userMapper.deleteByPrimaryKey(Long.valueOf(id))>0?true:false;
    }

    @Override
    @Transactional
    public Boolean addUser(User user,String userType) {
        int insert = userMapper.insert(user);
        //获取到插入数据库的用户的id
        UserExample example=new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPasswordEqualTo(user.getPassword());
        criteria.andNameEqualTo(user.getName());
        List<User> users = userMapper.selectByExample(example);
        Long id = users.get(0).getId();
        //获取userType所对应的id
        RoleExample roleExample = new RoleExample();
         roleExample.createCriteria().andNameEqualTo(userType.trim());
        List<Role> roles = roleMapper.selectByExample(roleExample);
        Long rid = roles.get(0).getId();
        //插入到角色表中
        UserRoleKey userRoleKey=new UserRoleKey();
        userRoleKey.setRid(rid);
        userRoleKey.setUid(id);
        userRoleMapper.insertSelective(userRoleKey);
        return insert>0?true:false;
    }

    @Override
    public Boolean modifyUser(User user) {

        int i = userMapper.updateByPrimaryKey(user);
        System.out.println(i+"========="+user);
        return i>0?true:false;
    }

    @Override
    public User findUserById(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        return user1;
    }

    @Override
    public User getUserByName(String username) {

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        System.out.println(userList.get(0));
        return userList.get(0);
    }

    @Override
    //获取所有的角色信息
    public List<Role> getRoles() {
        List<Role> roles = roleMapper.selectByExample(null);
        return roles;
    }
}
