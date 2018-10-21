package team.cloud.platform.service.impl;

import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.cloud.platform.constants.CommonConstants;
import team.cloud.platform.converter.Int2PodTypeConverter;
import team.cloud.platform.dao.PodMapper;
import team.cloud.platform.dao.ProjectMapper;
import team.cloud.platform.entity.Pod;
import team.cloud.platform.entity.Project;
import team.cloud.platform.entity.User;
import team.cloud.platform.enums.ImageEnums;
import team.cloud.platform.enums.PodTypeEnums;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.FileService;
import team.cloud.platform.service.NetService;
import team.cloud.platform.service.PodService;
import team.cloud.platform.service.UserService;
import team.cloud.platform.utils.KeyUtil;
import team.cloud.platform.utils.k8s.JerseyClient;
import team.cloud.platform.utils.k8s.K8sApi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
public class PodServiceImpl implements PodService {

    @Autowired
    private PodMapper podMapper;
    @Autowired
    private NetService netService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserService userService;

    /**
     * 向数据库插入容器信息
     *
     * @param userId  用户id
     * @param image   镜像
     * @param podType 容器类型
     * @return Pod信息
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Pod insertPod(Integer userId, String image, PodTypeEnums podType) {
        Pod pod = new Pod();
        String podName = KeyUtil.genUniqueKey();
        //容器目录，即挂载位置
        String storeLocation = CommonConstants.storeLocation;
        String volume = storeLocation + userId + "/" + podName;
        pod.setUserId(userId);
        pod.setPodName(podName);
        pod.setType(podType.getCode());
        pod.setImage(image);
        pod.setDirSrc(volume);
        pod.setExist(true);
        //设置容器停止时间为1小时后
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,1);
        pod.setEndTime(calendar.getTime());
        //写入数据库
        podMapper.insertPod(pod);
        return pod;
    }

    /**
     * 删除容器
     *
     * @param podId podId
     * @throws CommonException 删除失败
     */
    @Override
    public void deletePod(Integer podId) {
        Pod pod = podMapper.getPodByPodId(podId);
        //1.删除数据库信息
        podMapper.deletePodByPodId(podId);
        if(podMapper.getExistByPodId(podId)){
            //2.删除app 3.删除硬盘数据
            if (!K8sApi.delete(JerseyClient.getPodApi(), pod.getPodName()) || !K8sApi.delete(JerseyClient.getServiceApi(), pod.getPodName()) || !fileService.deleteFile(new File(pod.getDirSrc()))){
                throw new CommonException(ResultEnums.POD_DELETE_FAIL);
            }
        }
        else{
            //2.删除硬盘数据
            if (!K8sApi.delete(JerseyClient.getServiceApi(), pod.getPodName()) || !fileService.deleteFile(new File(pod.getDirSrc()))){
                throw new CommonException(ResultEnums.POD_DELETE_FAIL);
            }
        }
    }

    /**
     * 获取容器信息
     *
     * @param podId podId
     * @return Pod信息
     */
    @Override
    public Pod getPodByPodId(Integer podId) {
        return podMapper.getPodByPodId(podId);
    }

    /**
     * 启动的容器数量
     *
     * @return num
     */
    @Override
    public int countPodByExist() {
        return podMapper.countPodByExist(true);
    }

    /**
     * 根据PodId更新用户自定义名称
     *
     * @param name  用户自定义名称
     * @param podId podId
     */
    @Override
    public void updateNameByPodId(String name, Integer podId) {
        if (podMapper.updateNameByPodId(name,podId) < 1){
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
    }

    /**
     * 根据用户id和容器类型查询容器信息
     *
     * @param userId 用户id
     * @param type 容器类型
     * @return 容器信息列表
     */
    @Override
    public List<Pod> listPodByUserIdAndType(Integer userId, Integer type) {
        List<Pod> podList = podMapper.listPodByUserIdAndType(userId,type);
        if(podList!=null){
            for(Pod pod:podList){
                System.out.println(pod);
                startPod(pod.getPodId());
                podMapper.updateIpPortByPodName(K8sApi.getIp(pod.getPodName()),pod.getPodName());
            }
        }
        return podList;
    }

    /**
     * 查询所有用户容器信息
     *
     * @return 所有用户容器信息
     */
    @Override
    public List<Pod> listAllPod() {
        List<Pod> podList = podMapper.listAllPod();
        List<Pod> newPodList = new ArrayList<>();
        for (Pod pod : podList){
            Integer userId = pod.getUserId();
            User user = userService.getUserByUserId(userId);
            pod.setUser(user);
            newPodList.add(pod);
        }
        return newPodList;
    }

    /**
     * 停止容器（删除但保留数据库信息）
     *
     * @param podId podId
     */
    @Override
    public void stopPod(Integer podId) {
        Pod pod = podMapper.getPodByPodId(podId);
        if(podMapper.getExistByPodId(podId)){
            K8sApi.deletePod(JerseyClient.getPodApi(),pod.getPodName());
        }
        podMapper.updateExistByPodId(false,podId);
    }

    /**
     * 通用创建容器方法(不用上传项目文件)
     *
     * @param userId  用户id
     * @param image   镜像名
     * @param podType 容器类型
     * @return Pod信息
     */
    @Override
    public Pod commonCreatePod(Integer userId, String image, PodTypeEnums podType) {
        Pod pod = insertPod(userId, image, podType);
        commonCreatePod(pod.getPodName(),image,pod.getDirSrc(),podType,null);
        commonCreateService(pod.getPodName(), podType);
        podMapper.updateIpPortByPodName(K8sApi.getIp(pod.getPodName()),pod.getPodName());
        return pod;
    }

    /**
     * 通用创建容器方法(需要同时上传项目文件)
     *
     * @param userId        用户id
     * @param image         镜像名
     * @param podType       容器类型
     * @param multipartFile 项目文件
     * @return Pod信息
     */
    @Override
    public Pod commonCreatePod(Integer userId, String image, PodTypeEnums podType, MultipartFile multipartFile) {
        //1.先向数据库写入容器信息
        Pod pod = insertPod(userId, image, podType);
        try {
            //2.解压项目

            String projectDirSrc = pod.getDirSrc() + "/" +KeyUtil.genUniqueKey();
            fileService.unzipProjectFile(multipartFile,projectDirSrc,pod.getDirSrc());
            //3.创建容器
            commonCreatePod(pod.getPodName(),image,projectDirSrc,podType,null);
            commonCreateService(pod.getPodName(),podType);
            podMapper.updateIpPortByPodName(K8sApi.getIp(pod.getPodName()), pod.getPodName());
            //3.项目信息写入数据库
            Project project = new Project();
            project.setProjectName(multipartFile.getOriginalFilename());
            project.setPodName(pod.getPodName());
            project.setProjectAddress(projectDirSrc);
            projectMapper.insertProject(project);
            return pod;
        } catch (IOException | ZipException e) {
            e.printStackTrace();
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
    }

    /**
     * 封装k8s创建容器的api
     *
     * @param podName podName
     * @param image   镜像名
     * @param volume  映射目录
     * @param podType 容器类型
     * @param dllName dllName
     */
    @Override
    public void commonCreatePod(String podName, String image, String volume, PodTypeEnums podType, String dllName) {
        switch (podType){
            case NET:
                netService.createPod(podName, image, volume);
                break;
            default:
                K8sApi.runApp(podType, podName, image, volume, dllName);
                break;
        }
    }

    /**
     * 封装k8s创建service的api
     *
     * @param podName podName
     * @param podType 容器类型
     */
    @Override
    public void commonCreateService(String podName, PodTypeEnums podType) {
        switch (podType){
            case TOMCAT:
                K8sApi.runService(podName, 8080);
                break;
            case MYSQL:
                K8sApi.runService(podName, 3306);
                break;
            default:
                K8sApi.runService(podName, 80);
                break;
        }
    }

    /**
     * 通用重启容器方法
     *
     * @param podId podId
     */
    @Override
    public void commonRestartPod(Integer podId) {
        Pod pod = podMapper.getPodByPodId(podId);
        String podName = pod.getPodName();
        if(podMapper.getExistByPodId(podId)){
            K8sApi.deletePod(JerseyClient.getPodApi(),podName);
        }
        String image = pod.getImage();
        String volume = pod.getDirSrc();
        PodTypeEnums podTypeEnum = Int2PodTypeConverter.convert(pod.getType());
        commonCreatePod(podName,image,volume,podTypeEnum,null);
        podMapper.updateExistByPodId(true,pod.getPodId());
        podMapper.updateIpPortByPodName(K8sApi.getIp(podName),podName);
    }

    /**
     * 用户登录启动容器
     *
     * @param podId podId
     */
    @Override
    public void startPod(Integer podId) {
        if(!podMapper.getExistByPodId(podId)){
            Pod pod = podMapper.getPodByPodId(podId);
            String podName = pod.getPodName();
            String image = pod.getImage();
            String volume = pod.getDirSrc();
            PodTypeEnums podTypeEnum = Int2PodTypeConverter.convert(pod.getType());
            commonCreatePod(podName,image,volume,podTypeEnum,null);
            podMapper.updateExistByPodId(true,pod.getPodId());
            podMapper.updateIpPortByPodName(K8sApi.getIp(podName),podName);
        }
    }

    /**
     * 获取容器日志
     *
     * @param podId podId
     * @return 容器日志
     */
    @Override
    public String getPodLog(Integer podId) {
        if(podMapper.getExistByPodId(podId)){
            return K8sApi.getLog(podMapper.getPodByPodId(podId).getPodName());
        }else{
            return "容器未启动";
        }
    }

    /**
     * 获取容器状态
     *
     * @param podId podId
     * @return 容器状态
     */
    @Override
    public String getPodStatus(Integer podId) {
        if(podMapper.getExistByPodId(podId)){
            return K8sApi.getStatus(podMapper.getPodByPodId(podId).getPodName());
        }else{
            return "容器未启动";
        }
    }

    /**
     * 判断用户是否能创建PHP容器
     *
     * @param userId  userId
     * @return Boolean
     */
    @Override
    public Boolean createPhpPod(Integer userId) {
        String image = ImageEnums.PHP.getDescription();
        Integer podType = PodTypeEnums.PHP.getCode();
        int i = podMapper.countPodByUserIdAndType(userId, podType);
        if(i<1){
            return false;
        }else {
            PodTypeEnums podTypeEnum = Int2PodTypeConverter.convert(podType);
            commonCreatePod(userId, image, podTypeEnum);
            return true;
        }
    }
}
