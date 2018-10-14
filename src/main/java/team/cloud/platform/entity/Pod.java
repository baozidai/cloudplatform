package team.cloud.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@ApiModel("Pod")
@Data
public class Pod {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Integer podId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 容器名称
     */
    @ApiModelProperty("容器名称")
    private String podName;

    /**
     * 用户自定义名称
     */
    @ApiModelProperty("用户自定义名称")
    private String name = "未命名";

    /**
     * 容器端口号
     */
    @ApiModelProperty("容器端口号")
    private String ipPort;

    /**
     * 容器类型(java、php等)
     */
    @ApiModelProperty("容器类型")
    private Integer type;

    /**
     * 镜像名
     */
    @ApiModelProperty("镜像名")
    private String image;

    /**
     * 主机映射文件夹路径
     */
    @ApiModelProperty("主机映射文件夹路径")
    private String dirSrc;

    /**
     * 启动时间
     */
    @ApiModelProperty("启动时间")
    private Date startTime;

    /**
     * 停止时间
     */
    @ApiModelProperty("停止时间")
    private Date endTime;

    /**
     * 是否存在
     */
    @ApiModelProperty("是否存在")
    private Boolean exist;


    /**
     * 用户
     */
    @ApiModelProperty("用户")
    private User user;
}
