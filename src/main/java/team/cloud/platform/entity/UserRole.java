package team.cloud.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@ApiModel("UserRole")
@Data
public class UserRole {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Integer userRoleId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Integer roleId;
}
