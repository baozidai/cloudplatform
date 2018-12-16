package team.cloud.platform.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import team.cloud.platform.entity.Result;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.PodService;
import team.cloud.platform.service.ProjectService;
import team.cloud.platform.utils.web.ResultUtil;

import javax.servlet.http.HttpSession;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Api
@RestController
@RequestMapping(value = "/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PodService podService;

    /**
     * 通用上传项目方法
     *
     * @param session session
     * @param multipartFile 项目文件
     * @return 结果集
     */
    @ApiOperation(value = "通用上传项目方法")
    @PostMapping(value = "/upload")
    public Result commonUploadProject(HttpSession session,
                                      @RequestParam("filename") MultipartFile multipartFile){
        Integer podId = (Integer) session.getAttribute("uploadPodId");
        try{
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, projectService.commonUploadProject(podId, multipartFile));
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 删除项目
     *
     * @param projectId projectId
     * @return 结果集
     */
    @ApiOperation(value = "删除项目")
    @DeleteMapping(value = "/{projectId}")
    public Result deleteProject(@PathVariable("projectId") Integer projectId){
        try{
            projectService.deleteProjectByProjectId(projectId);
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 查询某个容器的用户的项目
     *
     * @param session session
     * @return 结果集
     */
    @ApiOperation(value = "查询某个容器的用户的项目")
    @GetMapping(value = "/pod")
    public Result listProject(HttpSession session){
        Integer podId = (Integer) session.getAttribute("podId");
        String podName = podService.getPodByPodId(podId).getPodName();
        return ResultUtil.success(ResultEnums.COMMON_SUCCESS, projectService.getProjectByPodName(podName));
    }

    /**
     * 用户跳转到单个容器的项目的界面
     *
     * @param podId podId
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个容器的项目的界面")
    @GetMapping()
    public ModelAndView project(@RequestParam("podId") Integer podId, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        session.setAttribute("podId", podId);
        mv.addObject("podId", podId);
        if(podService.getPodByPodId(podId).getType() == 1){
            mv.setViewName("tomcatProject");
        }else if(podService.getPodByPodId(podId).getType() == 2){
            mv.setViewName("phpProject");
        }
        return mv;
    }

    /**
     * 用户跳转到单个tomcat容器上传项目的界面
     *
     * @param podId podId
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个tomcat容器上传项目的界面")
    @GetMapping(value = "/tomcat_upload_file")
    public ModelAndView tomcatFileUpload(@RequestParam("podId") Integer podId, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        session.setAttribute("uploadPodId", podId);
        mv.addObject("podId", podId);
        mv.setViewName("tomcatFileUpload");
        return mv;
    }

    /**
     * 用户跳转到单个php容器上传项目的界面
     *
     * @param podId podId
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个php容器上传项目的界面")
    @GetMapping(value = "/php_upload_file")
    public ModelAndView phpFileUpload(@RequestParam("podId") Integer podId, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        session.setAttribute("uploadPodId", podId);
        mv.addObject("podId", podId);
        mv.setViewName("phpFileUpload");
        return mv;
    }

    /**
     * 用户跳转到单个python容器上传项目的界面
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个python容器上传项目的界面")
    @GetMapping(value = "/python_upload_file")
    public ModelAndView pythonFileUpload(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("pythonFileUpload");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 用户跳转到单个net容器上传项目的界面
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个net容器上传项目的界面")
    @GetMapping(value = "/net_upload_file")
    public ModelAndView netFileUpload(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("netFileUpload");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 用户跳转到单个net容器上传项目的界面
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个net容器上传项目的界面")
    @GetMapping(value = "/jar_upload_file")
    public ModelAndView jarFileUpload(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("jarFileUpload");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }
}