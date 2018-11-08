package team.cloud.platform.service;

import org.springframework.web.multipart.MultipartFile;
import team.cloud.platform.entity.Pod;
import team.cloud.platform.enums.PodTypeEnums;
import team.cloud.platform.exception.CommonException;

import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
public interface PodService {

    /**
     * 向数据库插入容器信息
     *
     * @param userId 用户id
     * @param image 镜像
     * @param podType 容器类型
     * @return Pod信息
     */
    Pod insertPod(Integer userId, String image, PodTypeEnums podType);

    /**
     * 删除容器
     *
     * @param podId podId
     * @throws CommonException 删除失败
     */
    void deletePod(Integer podId);

    /**
     * 获取容器信息
     *
     * @param podId podId
     * @return Pod信息
     */
    Pod getPodByPodId(Integer podId);

    /**
     * 启动的容器数量
     *
     * @return num
     */
    int countPodByExist();

    /**
     * 根据PodId更新用户自定义名称
     *
     * @param name 用户自定义名称
     * @param podId podId
     */
    void updateNameByPodId(String name, Integer podId);

    /**
     * 根据用户id和容器类型查询容器信息
     *
     * @param userId 用户id
     * @param type 容器类型
     * @return 容器信息列表
     */
    List<Pod> listPodByUserIdAndType(Integer userId, Integer type);

    /**
     * 查询所有用户容器信息
     *
     * @return 所有用户容器信息
     */
    List<Pod> listAllPod();

    /**
     * 停止容器（删除但保留数据库信息）
     *
     * @param podId podId
     */
    void stopPod(Integer podId);

    /**
     * 通用创建容器方法(不用上传项目文件)
     *
     * @param userId 用户id
     * @param image 镜像名
     * @param podType 容器类型
     * @return Pod信息
     */
    Pod commonCreatePod(Integer userId, String image, PodTypeEnums podType);

    /**
     * 通用创建容器方法(需要同时上传项目文件)
     *
     * @param userId 用户id
     * @param image 镜像名
     * @param podType 容器类型
     * @param multipartFile 项目文件
     * @return Pod信息
     */
    Pod commonCreatePod(Integer userId, String image, PodTypeEnums podType, MultipartFile multipartFile);

    /**
     * 封装k8s创建容器的api
     * @param podName podName
     * @param image 镜像名
     * @param volume 映射目录
     * @param podType 容器类型
     * @param dllName dllName
     */
    void commonCreatePod(String podName, String image, String volume, PodTypeEnums podType,String dllName);

    /**
     * 封装k8s创建service的api
     *
     * @param podName podName
     * @param podType 容器类型
     */
    void commonCreateService(String podName, PodTypeEnums podType);

    /**
     * 通用重启容器方法
     *
     * @param podId podId
     */
    void commonRestartPod(Integer podId);

    /**
     * 用户登录启动容器
     *
     * @param podId podId
     */
    void startPod(Integer podId);

    /**
     * 获取容器日志
     *
     * @param podId podId
     * @return 容器日志
     */
    String getPodLog(Integer podId);

    /**
     * 获取容器状态
     *
     * @param podId podId
     * @return 容器状态
     */
    String getPodStatus(Integer podId);

    /**
     * 判断用户是否能创建PHP容器
     *
     * @param userId userId
     * @return Boolean
     */
    Boolean createPhpPod(Integer userId);

    /**
     * 获取所有用户的姓名
     *
     * @param userName 姓名
     * @return pod集合
     */
    List<Pod> getUserPods(String userName);
}
