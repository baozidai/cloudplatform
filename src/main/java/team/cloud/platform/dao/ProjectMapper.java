package team.cloud.platform.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.cloud.platform.entity.Project;

import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Repository
public interface ProjectMapper {

    /**
     * 查询项目地址通过主键
     *
     * @param projectId projectId
     * @return 项目地址
     */
    String getProjectAddressByProjectId(@Param("projectId") Integer projectId);

    /**
     * 查询一个用户的项目通过podName
     *
     * @param podName 容器名
     * @return 容器列表
     */
    List<Project> getProjectByPodName(@Param("podName") String podName);

    /**
     * 根据projectId查询项目
     *
     * @param projectId projectId
     * @return 项目信息
     */
    Project getProjectByProjectId(@Param("projectId") Integer projectId);

    /**
     * 根据主键删除一条用户项目数据
     *
     * @param projectId projectId
     * @return 删除成功条数
     */
    int deleteProjectByProjectId(@Param("projectId") Integer projectId);

    /**
     * 插入一条完整的用户项目数据，id自增长
     *
     * @param project 项目
     * @return 插入成功条数
     */
    int insertProject(Project project);
}
