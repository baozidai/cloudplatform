package team.cloud.platform.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.cloud.platform.entity.Result;
import team.cloud.platform.entity.User;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.UserService;
import team.cloud.platform.utils.web.ResultUtil;

import javax.servlet.http.HttpSession;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Api
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param userName 用户名
     * @param userPassword 密码
     * @param session session
     * @return 结果集
     */
    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public Result userLogin(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword, HttpSession session){
        try{
            String roleName = userService.userLogin(userName,userPassword,session);
            return ResultUtil.success(ResultEnums.LOGIN_SUCCESS, roleName);
        }catch(CommonException e){
            return ResultUtil.error(e.getCode(), e.getMessage());
        }

    }

    /**
     * 用户注册
     *
     * @param user user
     * @return 结果集
     */
    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/user")
    public Result userRegister(User user, @RequestParam("roleId") Integer roleId) {
        try{
            userService.insertUser(user, roleId);
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户Id
     * @return 结果集
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/{userId}")
    public Result deleteUser(@PathVariable("userId") Integer userId) {
        try {
            userService.deleteUserByUserId(userId);
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 根据角色查询所有用户数据
     *
     * @param roleName 角色类型
     * @param session session
     * @return 用户集合
     */
    @ApiOperation(value = "根据角色查询所有用户数据")
    @GetMapping()
    public Result listUser(@RequestParam("roleName") String roleName, HttpSession session) {
        String roleName1 = "管理员";
        String rN = (String) session.getAttribute("roleName");
        if( roleName1.equals(rN)){
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS, userService.listAllUser(roleName));
        }else{
            return ResultUtil.error(ResultEnums.COMMON_FAIL);
        }
    }

    /**
     * 查询一条用户数据通过主键
     *
     * @param userId 用户Id
     * @return 结果集
     */
    @ApiOperation(value = "查询一条用户数据通过主键")
    @GetMapping(value = "/{userId}")
    public Result ser(@PathVariable("userId") Integer userId) {
        return ResultUtil.success(ResultEnums.COMMON_SUCCESS, userService.getUserByUserId(userId));
    }

    /**
     * 根据主键更新一条用户数据
     *
     * @param user 用户数据
     * @return 结果集
     */
    @ApiOperation(value = "根据主键更新一条用户数据")
    @PutMapping(value = "/{userId}")
    public Result updateUser(User user) {
        try{
            userService.updateUserByUserId(user);
            return ResultUtil.success(ResultEnums.COMMON_SUCCESS);
        }catch (CommonException e){
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 获取user session
     *
     * @param session session
     * @return 结果集
     */
    @ApiOperation(value = "获取user session")
    @GetMapping(value = "/session")
    public Result sessionUser(HttpSession session){
        User user = (User) session.getAttribute("user");
        return ResultUtil.success(ResultEnums.COMMON_SUCCESS,user);
    }

    /**
     * 检查用户名是否重复
     *
     * @param userName 用户名
     * @return 结果集
     */
    @ApiOperation(value = "检查用户名是否重复")
    @PostMapping(value = "/check/userName")
    public boolean checkUserName(@RequestParam("userName") String userName) {
        return userService.checkUserName(userName);
    }

    /**
     * 用户跳转到个人中心
     *
     * @return 结果集
     */
    @ApiOperation(value = "用户跳转到个人中心")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView userInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("userSelf");
        return mv;
    }

    /**
     * 退出登录
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "退出登录")
    @GetMapping(value = "/logout")
    public ModelAndView logout(HttpSession session) {
        // 清除session
        session.invalidate();
        return new ModelAndView("redirect:/");
    }

    /**
     * 用户跳转到注册页面
     *
     * @return ModelAndView
     */
    @ApiOperation(value = "用户跳转到注册页面")
    @GetMapping(value = "/register")
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("register");
        return mv;
    }

    /**
     * 管理员跳转到用户列表
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "管理员跳转到用户列表")
    @GetMapping(value = "/user_list")
    public ModelAndView userList(HttpSession session) {
        String roleName1 = "管理员";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){

            mv.setViewName("adminUserList");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 管理员跳转到管理员列表
     *
     * @param session session
     * @return ModelAndView
     */
    @ApiOperation(value = "管理员跳转到管理员列表")
    @GetMapping(value = "/admin_list")
    public ModelAndView adminList(HttpSession session) {
        String roleName1 = "管理员";
        String rN = (String) session.getAttribute("roleName");
        ModelAndView mv = new ModelAndView();
        if( roleName1.equals(rN)){
            mv.setViewName("adminList");
            return mv;
        }else{
            mv.setViewName("../404");
            return mv;
        }
    }

    /**
     * 管理员获取userId session
     *
     * @param session session
     * @return 结果集
     */
    @ApiOperation(value = "管理员获取userId")
    @GetMapping(value = "/session/admin/userId")
    public Result sessionUserId(HttpSession session){
        Integer userId = (Integer) session.getAttribute("adminGetUserId");
        return ResultUtil.success(ResultEnums.COMMON_SUCCESS,userId);
    }
}
