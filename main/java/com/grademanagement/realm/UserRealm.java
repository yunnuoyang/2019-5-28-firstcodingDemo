package com.grademanagement.realm;

import com.grademanagement.mapper.PermissionMapper;
import com.grademanagement.mapper.RolePermissionMapper;
import com.grademanagement.pojo.Permission;
import com.grademanagement.pojo.Role;
import com.grademanagement.pojo.User;
import com.grademanagement.service.RoleService;
import com.grademanagement.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author DELL
 * doGetAuthenticationInfo
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    protected void clearCache(PrincipalCollection principals) {
        //获取到认证之后的信息
        PrincipalCollection collection = SecurityUtils.getSubject().getPrincipals();
        //手动调用clearCache方法进行 清空缓存操作
        super.clearCache(collection);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录时输入的用户名
        String username = principals.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (username != null) {
            //获取用户信息
            User user = userService.getUserByName(username);
            //获取角色信息
            List<Role> roles = roleService.selectRolesByUID(user.getId().intValue());
            roles.forEach(role -> {
                //获取角色的id
                Long id = role.getId();
                //根据角色的id获取到资源的许可
                Permission permission = permissionMapper.selectByPrimaryKey(id);
                info.addStringPermission(permission.getName());
            });


//			for(Role role:roles){
//				info.addRole(role.getName());//加入角色
//				System.out.println(role.getName());
//			}
            if (roles != null && !roles.isEmpty()) {
                for (Role role : roles) {
                    info.addRole(role.getName());//加入角色
                    System.out.println(role.getName());
                }
            }
            return info;
        }
        return null;
    }

    @Override
    public String getName() {
        return "userRealm";
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();//获取身份信息
        System.out.println(username);
        System.out.println("获取用户输入的用户名=======================" + username);
        //通过用户名获取到用户的密码信息
        //根据用户名到数据库查询密码信息

        User user1 = userService.getUserByName(username);
        System.out.println(user1.getPassword());
//		//将从数据库中查询到的信息封装到SimpleAuthenticationInfo
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, user1.getPassword(), getName());
//
//		SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(username, user1.getPassword(), ByteSource.Util.bytes(user1.getPassword_salt().getBytes()), getName());
        return info;
    }

}
