package team.cloud.platform.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.cloud.platform.entity.Result;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.impl.HostServiceImpl;
import team.cloud.platform.utils.web.ResultUtil;

/**
 * @author Ernest
 * @date 2018/10/14下午4:41
 */

@Api
@RestController
@RequestMapping(value = "/hosts")
public class HostController {
    
    @Autowired
    private HostServiceImpl hostService;

    /**
     * 检查是否能创建用户
     * 
     * @return 结果集
     */
    @ApiOperation(value = "检查是否能创建用户")
    @PostMapping()
    public Result checkHost(){
        if(hostService.checkHost()){
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }else {
            return ResultUtil.success(ResultEnums.HOST_LIMIT);
        }
    }
}
