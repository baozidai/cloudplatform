package team.cloud.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@ApiModel("Role")
@Data
public class Role {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Integer roleId;

    /**
     * 用户和管理员名字name
     */
    @ApiModelProperty("用户和管理员名字name")
    private String roleName;
}
