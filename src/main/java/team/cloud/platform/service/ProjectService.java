package team.cloud.platform.service;

import org.springframework.web.multipart.MultipartFile;
import team.cloud.platform.entity.Pod;
import team.cloud.platform.entity.Project;

import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
public interface ProjectService {

    /**
     * 插入一条完整的用户项目数据，id自增长
     *
     * @param project 项目
     */
    void insertProject(Project project);

    /**
     * 根据主键删除一条用户项目数据
     *
     * @param projectId 项目Id
     * @return 删除成功条数
     */
    int deleteProjectByProjectId(Integer projectId);

    /**
     * 查询一个用户的项目通过podName
     *
     * @param podName 容器名
     * @return 容器列表
     */
    List<Project> getProjectByPodName(String podName);

    /**
     * 通用上传项目方法
     *
     * @param pod 要上传到容器信息
     * @param multipartFile 项目文件
     * @return Project
     */
    Project commonUploadProject(Pod pod, MultipartFile multipartFile);

    /**
     * 通用上传项目方法
     *
     * @param podId podId
     * @param multipartFile 项目文件
     * @return Project
     */
    Project commonUploadProject(Integer podId, MultipartFile multipartFile);
}
