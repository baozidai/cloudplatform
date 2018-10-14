package team.cloud.platform.converter;

import team.cloud.platform.enums.PodTypeEnums;
import team.cloud.platform.exception.exception.Int2PodTypeEnumConvertException;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
public class Int2PodTypeConverter {

    public static PodTypeEnums convert(Integer code){
        for (PodTypeEnums podTypeEnums : PodTypeEnums.values()){
            if (code.equals(podTypeEnums.getCode())){
                return podTypeEnums;
            }
        }
        throw new Int2PodTypeEnumConvertException(code);
    }
}
