package team.cloud.platform.entity;

import lombok.Data;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@Data
public class Result<T> {

    /**
     * 提示码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 具体内容
     */
    private Object extend;
}
