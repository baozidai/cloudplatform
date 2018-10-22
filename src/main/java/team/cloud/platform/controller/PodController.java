package team.cloud.platform.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import team.cloud.platform.converter.Int2PodTypeConverter;
import team.cloud.platform.entity.Result;
import team.cloud.platform.enums.PodTypeEnums;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.PodService;
import team.cloud.platform.utils.web.ResultUtil;

import javax.servlet.http.HttpSession;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Api
@RestController
@RequestMapping(value = "/pods")
public class PodController {

    @Autowired
    private PodService podService;

    /**
     * 根据用户id和容器类型查询容器信息
     *
     * @param userId 用户id
     * @param type 容器类型
     * @param session session
     * @return 结果集
     */
    @ApiOperation(value = "根据用户id和容器类型查询容器信息")
    @GetMapping(value = "/users/{userId}")
    public Result listPodByUserIdAndType(@PathVariable("userId") Integer userId, @RequestParam("podType") Integer type, HttpSession session){
        Integer uI = (Integer) session.getAttribute("userId");
        if(!userId.equals(uI)){
            return ResultUtil.error(ResultEnums.COMMON_FAIL);
        }
        else{
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, podService.listPodByUserIdAndType(userId, type));
        }
    }

    /**
     * 查询所有容器信息
     *
     * @param session session
     * @return 结果集
     */
    @ApiOperation(value = "查询所有容器信息")
    @GetMapping()
    public Result listAllPod(HttpSession session){
        String roleName1 = "管理员";
        String rN = (String) session.getAttribute("roleName");
        if(roleName1.equals(rN)){
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, podService.listAllPod());
        }else{
            return ResultUtil.error(ResultEnums.COMMON_FAIL);
        }

    }

    /**
     * 通用创建容器方法(不用上传项目文件)
     *
     * @param image 镜像
     * @param podType 容器类型
     * @param userId 用户Id
     * @return 结果集
     */
    @PostMapping()
    public Result commonCreatePod(@RequestParam("image") String image,
                                        @RequestParam("podType") Integer podType,
                                        @RequestParam("userId") Integer userId){
        PodTypeEnums podTypeEnum = Int2PodTypeConverter.convert(podType);
        try{
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, podService.commonCreatePod(userId, image, podTypeEnum));
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 通用创建容器方法(需要上传项目文件)
     *
     * @param image 镜像
     * @param podType 容器类型
     * @param session session
     * @param multipartFile 文件
     * @return 结果集
     */
    @ApiOperation(value = "通用创建容器方法(需要上传项目文件)")
    @PostMapping(value = "/projects")
    public Result commonCreatePod(@RequestParam("image") String image,
                                        @RequestParam("podType") Integer podType,
                                        @RequestParam("filename") MultipartFile multipartFile,
                                        HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        PodTypeEnums podTypeEnum = Int2PodTypeConverter.convert(podType);
        try{
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, podService.commonCreatePod(userId, image, podTypeEnum,multipartFile));
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 判断用户是否能创建PHP容器并创建
     *
     * @param session session
     * @return 结果集
     */
    @ApiOperation(value = "判断用户是否能创建PHP容器并创建")
    @PostMapping(value = "/php/exist")
    public Result createPhpPod(HttpSession session){

        Integer userId = (Integer) session.getAttribute("userId");
        try {
            if (podService.createPhpPod(userId)) {
                return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
            } else {
                return ResultUtil.success(ResultEnums.PHP_EXIST_ERROR);
            }
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 通用重启容器方法
     *
     * @param podId podId
     * @return 结果集
     */
    @ApiOperation(value = "通用重启容器方法")
    @PutMapping(value = "/restart/{podId}")
    public Result commonRestartPod(@PathVariable("podId") Integer podId){
        try {
            podService.commonRestartPod(podId);
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 通用删除容器方法
     *
     * @param podId podId
     * @return 结果集
     */
    @ApiOperation(value = "通用删除容器方法")
    @DeleteMapping(value = "/{podId}")
    public Result deletePod(@PathVariable("podId") Integer podId){
        try {
            podService.deletePod(podId);
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 更新容器名通过podId
     *
     * @param name 容器名
     * @param podId podId
     * @return 结果集
     */
    @ApiOperation(value = "更新容器名通过podId")
    @PatchMapping(value = "/{podId}/name")
    public Result updateNameById(@RequestParam("name") String name, @PathVariable("podId") Integer podId){
        try {
            podService.updateNameByPodId(name, podId);
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 获取容器日志
     *
     * @param podId podId
     * @return 结果集
     */
    @ApiOperation(value = "获取容器日志")
    @GetMapping(value = "/{podId}/log")
    public Result getPodLog(@PathVariable("podId") Integer podId){
        try{
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, podService.getPodLog(podId));
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 获取容器状态
     *
     * @param podId podId
     * @return 结果集
     */
    @ApiOperation(value = "获取容器状态")
    @GetMapping(value = "/{podId}/status")
    public Result getPodStatus(@PathVariable("podId") Integer podId){
        try {
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, podService.getPodStatus(podId));
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 用户跳转到单个用户tomcat容器
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个用户tomcat容器")
    @GetMapping(value = "/tomcat")
    public ModelAndView tomcat(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("userTomcat");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 用户跳转到单个用户php容器
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个用户php容器")
    @GetMapping(value = "/php")
    public ModelAndView php(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("userPhp");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 用户跳转到单个用户Net容器
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个用户Net容器")
    @GetMapping(value = "/net")
    public ModelAndView net(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("userNet");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 用户跳转到单个用户Python容器
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个用户Python容器")
    @GetMapping(value = "/python")
    public ModelAndView python(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("userPython");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 用户跳转到单个用户mysql容器
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到单个用户mysql容器")
    @GetMapping(value = "/mysql")
    public ModelAndView mysql(HttpSession session) {
        String roleName1 = "用户";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("userMysql");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 管理员跳转到所有容器列表界面
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "管理员跳转到所有容器列表界面")
    @GetMapping(value = "/container_list")
    public ModelAndView containerList(HttpSession session) {
        String roleName1 = "管理员";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("adminContainerList");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }

    }

    /**
     * 管理员跳转到单个用户的容器列表界面
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "管理员跳转到单个用户的容器列表界面")
    @GetMapping(value = "/user_container_list")
    public ModelAndView userContainerList(@RequestParam("userId") Integer userId,HttpSession session) {
        String roleName1 = "管理员";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            session.setAttribute("adminGetUserId",userId);
            mv.addObject("userId", userId);
            mv.setViewName("adminUserContainerList");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }
}
