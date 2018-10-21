package team.cloud.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.cloud.platform.constants.CommonConstants;
import team.cloud.platform.dao.PodMapper;
import team.cloud.platform.dao.UserMapper;
import team.cloud.platform.entity.Pod;
import team.cloud.platform.entity.User;
import team.cloud.platform.entity.UserRole;
import team.cloud.platform.enums.ImageEnums;
import team.cloud.platform.enums.PodTypeEnums;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.enums.RoleEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.PodService;
import team.cloud.platform.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private PodServiceImpl podService;

    @Autowired
    private PodMapper podMapper;


    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param userPassword 密码
     * @param session session
     * @return 用户
     */
    @Override
    public String userLogin(String userName, String userPassword, HttpSession session) {
        User user = userMapper.getUserByUserNameAndUserPassword(userName, userPassword);
        if (user != null) {
            Integer userId = user.getUserId();

            Integer roleId = userRoleService.getRoleIdByUserId(userId);

            String roleName = roleService.getRoleNameByRoleId(roleId);
            ServletContext application = session.getServletContext();
            Set<String> onlineUserSet = new HashSet<String>();

            onlineUserSet = (Set)application.getAttribute("onlineUserSet");
            if(true){
                session.setAttribute("userId", userId);
                session.setAttribute("user", user);
                session.setAttribute("roleName", roleName);
                onlineUserSet.add(userName);
                application.setAttribute("onlineUserList", onlineUserSet);
                return roleName;
            }else{
                if(!onlineUserSet.add(userName)){
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("user", user);
                    return roleName;
                }else{
                    onlineUserSet.remove(userName);
                    throw new CommonException(ResultEnums.LOGIN_LIMIT);
                }
            }

        } else {
            throw new CommonException(ResultEnums.LOGIN_FAIL);
        }
    }

    /**
     * 插入一条完整用户数据 ,Id自增长
     *
     * @param user   用户信息
     * @param roleId 用户权限
     */
    @Override
    public void insertUser(User user, Integer roleId) {

        userMapper.insertUser(user);
        User newUser = userMapper.getUserByUserName(user.getUserName());
        Integer userId = newUser.getUserId();
        String roleName = roleService.getRoleNameByRoleId(roleId);
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        if (userRoleService.insertUserRole(userRole) < 1){
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
        if(roleName.equals(RoleEnums.USER.getDescription())){
            podService.commonCreatePod(userId, ImageEnums.TOMCAT.getDescription(), PodTypeEnums.TOMCAT);
            podService.commonCreatePod(userId, ImageEnums.MYSQL.getDescription(), PodTypeEnums.MYSQL);
            List<Integer> idList = podMapper.listPodIdByUserId(userId);

            if(idList!=null){
                for(Integer podId:idList){
                    podService.stopPod(podId);
                }
            }
        }
    }

    /**
     * 根据主键删除一条用户数据
     *
     * @param userId 用户Id
     */
    @Override
    public void deleteUserByUserId(Integer userId) {

        Integer roleId = userRoleService.getRoleIdByUserId(userId);
        String roleName = roleService.getRoleNameByRoleId(roleId);
        userRoleService.deleteUserRoleByUserId(userId);
        if (userMapper.deleteUserByUserId(userId) < 1){
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
        if (roleName.equals(RoleEnums.USER.getDescription())) {
            List<Integer> idList = podMapper.listPodIdByUserId(userId);
            if(idList!=null){
                for(Integer podId:idList){
                    podService.deletePod(podId);
                }
            }
        }
    }

    /**
     * 根据角色查询所有用户数据
     *
     * @param roleName 角色
     * @return List<User>
     */
    @Override
    public List<User> listAllUser(String roleName) {
        Integer roleId = roleService.getRoleIdByRoleName(roleName);
        List<Integer> useIdList= userRoleService.getUserIdByRoleId(roleId);
        List<User> userList = new ArrayList<>();
        for(Integer userId:useIdList){
            User user = userMapper.getUserByUserId(userId);
            userList.add(user);
        }
        return userList;
    }

    /**
     * 查询一条用户数据通过主键
     *
     * @param userId 用户Id
     * @return 用户数据
     */
    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.getUserByUserId(userId);
    }

    /**
     * 根据主键更新一条用户数据
     *
     * @param user 用户数据
     */
    @Override
    public void updateUserByUserId(User user) {
        if (userMapper.updateUserByUserId(user) < 1){
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
    }

    /**
     * 检查用户名是否重复
     *
     * @param userName 用户名
     * @return boolean
     */
    @Override
    public boolean checkUserName(String userName) {

        User user = userMapper.getUserByUserName(userName);
        return user == null;
    }
}
