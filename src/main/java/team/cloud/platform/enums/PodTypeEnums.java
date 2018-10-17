package team.cloud.platform.enums;

import lombok.Getter;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@Getter
public enum PodTypeEnums {

    /**
     * javaWeb类型
     */
    TOMCAT(1,"tomcat"),

    /**
     * phpWeb类型
     */
    PHP(2,"php"),

    /**
     * pythonWeb类型
     */
    PYTHON(3,"python"),

    /**
     * .netWeb类型
     */
    NET(4,"net"),

    /**
     * mysql数据库
     */
    MYSQL(5,"mysql")
    ;

    /**
     * 类型代码
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    PodTypeEnums(Integer code, String description){
        this.code = code;
        this.description = description;
    }
}
