package team.cloud.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.cloud.platform.service.HostService;
import team.cloud.platform.service.PodService;

import javax.xml.ws.Action;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private PodService podService;

    /**
     * 检查是否能创建用户
     *
     * @return Boolean值
     */
    @Override
    public Boolean checkHost() {
        return podService.countPodByExist() <= 14;
    }
}
