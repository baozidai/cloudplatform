package team.cloud.platform.enums;

import lombok.Getter;

/**
 * @author Ernest
 * @date 2018/9/16下午7:28
 */
@Getter
public enum HostIpEnums {

    /**
     * node1
     */
    HOST1(1,"10.2.132.172"),

    /**
     * node2
     */
    HOST2(2,"10.2.132.173"),
    ;

    /**
     * 类型代码
     */
    private Integer code;

    /**
     * node主机地址
     */
    private String ip;

    HostIpEnums(Integer code, String ip){
        this.code = code;
        this.ip = ip;
    }
}
