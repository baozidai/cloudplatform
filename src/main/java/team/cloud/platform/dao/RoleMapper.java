package team.cloud.platform.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Repository
public interface RoleMapper {

    /**
     * 查询角色通过roleId
     *
     * @param roleId roleId
     * @return 角色名称
     */
    String getRoleNameByRoleId(@Param("roleId") Integer roleId);

    /**
     * 查询roleId通过角色
     *
     * @param roleName roleName
     * @return roleId
     */
    int getRoleIdByRoleName(@Param("roleName") String roleName);
}
