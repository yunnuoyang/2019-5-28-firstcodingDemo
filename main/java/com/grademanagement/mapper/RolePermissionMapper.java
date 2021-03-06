package com.grademanagement.mapper;

import com.grademanagement.pojo.RolePermissionExample;
import com.grademanagement.pojo.RolePermissionKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionMapper {
    long countByExample(RolePermissionExample example);

    int deleteByExample(RolePermissionExample example);

    int deleteByPrimaryKey(RolePermissionKey key);

    int insert(RolePermissionKey record);

    int insertSelective(RolePermissionKey record);

    List<RolePermissionKey> selectByExample(RolePermissionExample example);

    int updateByExampleSelective(@Param("record") RolePermissionKey record, @Param("example") RolePermissionExample example);

    int updateByExample(@Param("record") RolePermissionKey record, @Param("example") RolePermissionExample example);
}