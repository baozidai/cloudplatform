package team.cloud.platform.enums;

import lombok.Getter;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Getter
public enum ImageEnums {

    /**
     * tomcat镜像
     */
    TOMCAT(1,"10.2.132.171:5000/tomcat:1.0"),

    /**
     * php镜像
     */
    PHP(2,"10.2.132.171:5000/php:1.0"),

    /**
     * python镜像
     */
    PYTHON(3,"10.2.132.171:5000/python:1.0"),

    /**
     * net镜像
     */
    NET(4,"10.2.132.171:5000/net:1.0"),

    /**
     * jdk8镜像
     */
    JAR(5,"10.2.132.171:5000/jdk8:1.0"),
    ;

    /**
     * 类型代码
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    ImageEnums(Integer code, String description){
        this.code = code;
        this.description = description;
    }
}
