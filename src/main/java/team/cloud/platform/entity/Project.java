package team.cloud.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@ApiModel("Project")
@Data
public class Project {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Integer projectId;

    /**
     * 容器名称
     */
    @ApiModelProperty("容器名称")
    private String podName;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;

    /**
     * 项目地址
     */
    @ApiModelProperty("项目地址")
    private String projectAddress;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;
}
