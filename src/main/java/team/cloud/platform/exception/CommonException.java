package team.cloud.platform.exception;

import lombok.Getter;
import team.cloud.platform.enums.ResultEnums;

/**
 * 通用异常返回信息
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Getter
public class CommonException extends RuntimeException{

    /**
     * 错误代码
     */
    private Integer code;

    public CommonException(ResultEnums resultEnums){
        super(resultEnums.getMsg());
        this.code = resultEnums.getCode();
    }
}
