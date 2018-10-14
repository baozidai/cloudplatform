package team.cloud.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.cloud.platform.dao.RoleMapper;
import team.cloud.platform.service.RoleService;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 查询角色通过roleId
     *
     * @param roleId roleId
     * @return 角色名称
     */
    @Override
    public String getRoleNameByRoleId(Integer roleId) {
        return roleMapper.getRoleNameByRoleId(roleId);
    }

    /**
     * 查询roleId通过角色
     *
     * @param roleName roleName
     * @return roleId
     */
    @Override
    public int getRoleIdByRoleName(String roleName) {

        return roleMapper.getRoleIdByRoleName(roleName);
    }
}
