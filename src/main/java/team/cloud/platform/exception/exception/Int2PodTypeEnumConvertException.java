package team.cloud.platform.exception.exception;

import lombok.Getter;

/**
 * @author Ernest
 * @date 2018/9/16下午7:30
 */
@Getter
public class Int2PodTypeEnumConvertException extends RuntimeException{

    /**
     * 转换源
     */
    private Integer source;

    public Int2PodTypeEnumConvertException(Integer source) {
        super("整数" + source + "--->PodTypeEnum 转换失败");
        this.source = source;
    }
}
