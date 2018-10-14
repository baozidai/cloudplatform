package team.cloud.platform.service.impl;

import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.cloud.platform.dao.PodMapper;
import team.cloud.platform.dao.ProjectMapper;
import team.cloud.platform.entity.Pod;
import team.cloud.platform.entity.Project;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.FileService;
import team.cloud.platform.service.PodService;
import team.cloud.platform.service.ProjectService;
import team.cloud.platform.utils.KeyUtil;

import javax.resource.spi.CommException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private PodService podService;

    /**
     * 插入一条完整的用户项目数据，id自增长
     *
     * @param project 项目
     */
    @Override
    public void insertProject(Project project) {
        if(projectMapper.insertProject(project)<1){
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
    }

    /**
     * 根据主键删除一条用户项目数据
     *
     * @param projectId 项目Id
     * @return 删除成功条数
     */
    @Override
    public int deleteProjectByProjectId(Integer projectId) {
        if (!fileService.deleteFile(new File(projectMapper.getProjectByProjectId(projectId).getProjectAddress()))){
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
        return projectMapper.deleteProjectByProjectId(projectId);
    }

    /**
     * 查询一个用户的项目通过podName
     *
     * @param podName 容器名
     * @return 容器列表
     */
    @Override
    public List<Project> getProjectByPodName(String podName) {
        return projectMapper.getProjectByPodName(podName);
    }

    /**
     * 通用上传项目方法
     *
     * @param pod 要上传到容器信息
     * @param multipartFile 项目文件
     * @return Project
     */
    @Override
    public Project commonUploadProject(Pod pod, MultipartFile multipartFile) {
        //1.保存项目压缩包,存储路径为容器文件夹下
        //得到项目文件夹路径（容器目录+项目id）
        String projectPath = pod.getDirSrc() + "/" + KeyUtil.genUniqueKey();
        String path = pod.getDirSrc();
        try {
            //2.在项目目录下解压
            String change = fileService.unzipProjectFile(multipartFile, projectPath,path);
            System.out.println(change);
            //3.插入项目表信息
            Project project = new Project();
            //项目名为压缩包名
            String originalFilename = multipartFile.getOriginalFilename();
            project.setProjectName(originalFilename.substring(0, originalFilename.indexOf(".")));
            project.setPodName(pod.getPodName());
            project.setProjectAddress(change);
            List<Project> projects = getProjectByPodName(pod.getPodName());
            if(projects.isEmpty()){
                projectMapper.insertProject(project);
                return project;
            }else{
                for(int i = 0; i < projects.size(); i++){
                    if(!change.equals(projects.get(i).getProjectAddress())){
                        if (i == projects.size() - 1) {
                            projectMapper.insertProject(project);
                            return project;
                        }
                    }else{
                        break;
                    }
                }
                return project;
            }
        } catch (IOException | ZipException e) {
            e.printStackTrace();
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
    }

    /**
     * 通用上传项目方法
     *
     * @param podId            podId
     * @param multipartFile 项目文件
     * @return Project
     */
    @Override
    public Project commonUploadProject(Integer podId, MultipartFile multipartFile) {
        return commonUploadProject(podService.getPodByPodId(podId),multipartFile);
    }
}
