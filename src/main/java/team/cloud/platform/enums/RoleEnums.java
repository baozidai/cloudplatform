package team.cloud.platform.enums;

import lombok.Getter;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Getter
public enum RoleEnums {

    /**
     * 管理员身份
     */
    ADMIN(1,"管理员"),

    /**
     * 用户身份
     */
    USER(2,"用户"),
    ;

    /**
     * 类型代码
     */
    private Integer code;

    /**
     * 用户身份
     */
    private String description;

    RoleEnums(Integer code, String description){
        this.code = code;
        this.description = description;
    }
}
