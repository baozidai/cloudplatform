package team.cloud.platform.enums;

import lombok.Getter;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Getter
public enum ResultEnums {

    /**
     * 登录成功
     */
    LOGIN_SUCCESS(1, "登录成功"),

    /**
     * 登录失败
     */
    LOGIN_FAIL(2, "登录失败，用户名或密码错误"),

    /**
     * 登录受限
     */
    LOGIN_LIMIT(3, "登录受限，请稍后尝试！"),

    /**
     * 通用返回成功结果集
     */
    COMMON_SUCCESS(4, "成功"),

    /**
     * 通用返回失败结果集
     */
    COMMON_FAIL(5, "失败"),

    /**
     * 通用返回异常结果集
     */
    COMMON_EXCEPTION(6, "服务器异常，请稍后尝试"),

    /**
     * 不支持
     */
    UNSUPPORTED_POD_TYPE(7, "不支持"),

    /**
     * 容器删除失败
     */
    POD_DELETE_FAIL(8, "容器删除失败"),

    /**
     * 容器未启动
     */
    POD_NOT_FOUND(9, "未启动"),

    /**
     * php容器已存在
     */
    PHP_EXIST_ERROR(10, "已存在一个PHP容器"),

    /**
     * 当前资源已满，请稍后尝试
     */
    HOST_LIMIT(11, "当前资源已满，请稍后尝试")
    ;
    private Integer code;

    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
