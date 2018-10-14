package team.cloud.platform.service;

import team.cloud.platform.exception.CommonException;

import java.io.File;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
public interface NetService {

    /**
     * 创建.net容器，封装k8s api
     *
     * @param podName podName
     * @param image 镜像名
     * @param podDir 容器目录
     * @throws CommonException 没找到dll，创建失败
     */
    void createPod(String podName, String image, String podDir) throws CommonException;

    /**
     * 扫描dll文件，递归方式
     *
     * @param destFile 要扫描的文件或文件夹
     * @return 扫描到返回dll文件，没扫描到返回null
     */
    File getDllFile(File destFile);
}
