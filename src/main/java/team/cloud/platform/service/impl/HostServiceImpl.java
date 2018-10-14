package team.cloud.platform.service.impl;

import org.springframework.stereotype.Service;
import team.cloud.platform.service.HostService;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
public class HostServiceImpl implements HostService {
    /**
     * 检查是否能创建用户
     *
     * @return Boolean值
     */
    @Override
    public Boolean checkHost() {
        if(true){
            return true;
        }else{
            return false;
        }
    }
}
