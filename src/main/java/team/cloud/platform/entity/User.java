package team.cloud.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@ApiModel("User")
@Data
public class User {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 用户name
     */
    @ApiModelProperty("用户name")
    private String userName;

    /**
     * 用户password
     */
    @ApiModelProperty("用户password")
    private String userPassword;

    /**
     * 队伍名字
     */
    @ApiModelProperty("队伍名字")
    private String teamName;

    /**
     * 用户电话
     */
    @ApiModelProperty("用户电话")
    private String userPhone;

    /**
     * 用户班级
     */
    @ApiModelProperty("用户班级")
    private String userClass;

}
