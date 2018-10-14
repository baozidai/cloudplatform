package team.cloud.platform.utils.web;

import team.cloud.platform.entity.Result;
import team.cloud.platform.enums.ResultEnums;

/**
 * @author Ernest
 * @date 2018/9/16下午7:34
 */
public class ResultUtil {

    public static Result success(ResultEnums resultEnums, Object object){
        Result result = new Result();
        result.setCode(resultEnums.getCode());
        result.setMsg(resultEnums.getMsg());
        result.setExtend(object);
        return result;
    }

    public static Result success(ResultEnums resultEnums){

        return success(resultEnums, null);
    }

    public static Result success(Object object){
        Result result = new Result();
        result.setCode(ResultEnums.COMMON_SUCCESS.getCode());
        result.setMsg(ResultEnums.COMMON_SUCCESS.getMsg());
        result.setExtend(object);
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(ResultEnums.COMMON_SUCCESS.getCode());
        result.setMsg(ResultEnums.COMMON_SUCCESS.getMsg());
        return result;
    }

    public static Result error(ResultEnums resultEnums){
        Result result = new Result();
        result.setCode(resultEnums.getCode());
        result.setMsg(resultEnums.getMsg());
        return result;
    }

    public static Result error(Integer code, String message){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    public static Result error(String message){
        Result result = new Result();
        result.setCode(ResultEnums.COMMON_EXCEPTION.getCode());
        result.setMsg(message);
        return result;
    }
}
