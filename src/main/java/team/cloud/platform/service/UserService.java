package team.cloud.platform.service;

import team.cloud.platform.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param userPassword 密码
     * @param session session
     * @return 用户
     */
    String userLogin(String userName, String userPassword, HttpSession session);

    /**
     * 插入一条完整用户数据 ,Id自增长
     *
     * @param user 用户信息
     * @param roleId 用户权限
     */
    void insertUser(User user, Integer roleId);

    /**
     * 根据主键删除一条用户数据
     *
     * @param userId 用户Id
     */
    void deleteUserByUserId(Integer userId);

    /**
     * 根据角色查询所有用户数据
     *
     * @param roleName 角色
     * @return List<User>
     */
    List<User> listAllUser(String roleName);

    /**
     * 查询一条用户数据通过主键
     *
     * @param userId 用户Id
     * @return 用户数据
     */
    User getUserByUserId(Integer userId);

    /**
     * 根据主键更新一条用户数据
     *
     * @param user 用户数据
     */
    void updateUserByUserId(User user);

    /**
     * 检查用户名是否重复
     *
     * @param userName 用户名
     * @return boolean
     */
    boolean checkUserName(String userName);
}
