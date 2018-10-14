package team.cloud.platform.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.cloud.platform.entity.UserRole;

import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Repository
public interface UserRoleMapper {

    /**
     * 根据userId删除一条用户数据
     *
     * @param userId 用户Id
     * @return 删除成功的条数
     */
    int deleteUserRoleByUserId(@Param("userId") Integer userId);

    /**
     * 插入一条完整用户角色数据 ,Id自增长
     *
     * @param userRole 用户角色
     * @return 插入成功的条数
     */
    int insertUserRole(UserRole userRole);

    /**
     * 查询roleId通过userId(登录)
     *
     * @param userId 用户Id
     * @return roleId
     */
    int getRoleIdByUserId(@Param("userId") Integer userId);

    /**
     * 查询userId通过roleId
     *
     * @param roleId roleId
     * @return userId集合
     */
    List<Integer> getUserIdByRoleId(@Param("roleId") Integer roleId);
}
