package team.cloud.platform.service;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
public interface RoleService {

    /**
     * 查询角色通过roleId
     *
     * @param roleId roleId
     * @return 角色名称
     */
    String getRoleNameByRoleId(Integer roleId);

    /**
     * 查询roleId通过角色
     *
     * @param roleName roleName
     * @return roleId
     */
    int getRoleIdByRoleName(String roleName);
}
