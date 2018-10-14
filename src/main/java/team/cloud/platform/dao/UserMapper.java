package team.cloud.platform.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.cloud.platform.entity.User;

import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Repository
public interface UserMapper {

    /**
     * 插入一条完整用户数据 ,Id自增长
     *
     * @param user 用户信息
     * @return 插入成功的条数
     */
    int insertUser(User user);

    /**
     * 根据主键删除一条用户数据
     *
     * @param userId 主键userId
     * @return 删除成功的记录数量
     */
    int deleteUserByUserId(@Param("userId") Integer userId);

    /**
     * 查询所有用户数据
     *
     * @return 所有用户数据
     */
    List<User> listAllUser();

    /**
     * 根据用户名查询一条用户的数据
     *
     * @param userName 用户名
     * @return 用户数据
     */
    User getUserByUserName(@Param("userName") String userName);

    /**
     *查询一条用户数据通过主键
     *
     *@param userId 用户id
     *@return 用户数据
     */
    User getUserByUserId(@Param("userId") Integer userId);

    /**
     * 查询user通过userName和userPassword
     *
     * @param  userName 用户姓名
     * @param  userPassword 用户密码
     * @return 用户id
     */
    User getUserByUserNameAndUserPassword(@Param("userName") String userName, @Param("userPassword") String userPassword);

    /**
     * 根据主键更新一条用户数据
     *
     * @param user 用户数据
     * @return 更新成功条数
     */
    int updateUserByUserId(User user);
}
