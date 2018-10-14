package team.cloud.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.cloud.platform.dao.UserRoleMapper;
import team.cloud.platform.entity.UserRole;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.UserRoleService;

import javax.resource.spi.CommException;
import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 查询roleId通过userId(登录)
     *
     * @param userId 用户Id
     * @return roleId
     */
    @Override
    public int getRoleIdByUserId(Integer userId) {
        return userRoleMapper.getRoleIdByUserId(userId);
    }

    /**
     * 插入一条完整用户角色数据 ,Id自增长
     *
     * @param userRole 用户角色
     * @return 插入成功的条数
     */
    @Override
    public int insertUserRole(UserRole userRole) {
        return userRoleMapper.insertUserRole(userRole);

    }

    /**
     * 根据userId删除一条用户数据
     *
     * @param userId 用户Id
     */
    @Override
    public void deleteUserRoleByUserId(Integer userId) {
        if(userRoleMapper.deleteUserRoleByUserId(userId)<1){
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
    }

    /**
     * 查询userId通过roleId
     *
     * @param roleId roleId
     * @return userId集合
     */
    @Override
    public List<Integer> getUserIdByRoleId(Integer roleId) {

        return userRoleMapper.getUserIdByRoleId(roleId);
    }
}
