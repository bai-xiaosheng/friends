package com.example.usercentor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercentor.common.BaseResponse;
import com.example.usercentor.common.Code;
import com.example.usercentor.common.ResultUtils;
import com.example.usercentor.exception.BusinessException;
import com.example.usercentor.modal.domain.User;
import com.example.usercentor.modal.domain.request.UserLoginRequest;
import com.example.usercentor.modal.domain.request.UserRegister;
import com.example.usercentor.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.usercentor.constant.Constant.ADMIN_ROLE;
import static com.example.usercentor.constant.Constant.USER_LOGIN_STATE;

/**
 * 用户控制
 *
 * @author BDS
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(Code.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        return ResultUtils.success(userService.userLogin(userAccount, userPassword, request));
    }
    @PostMapping("/loginOut")
    public BaseResponse<Integer> userLogout(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(Code.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.loginOut(request));
    }
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegister userRegister) {
        if (userRegister == null) {
            throw new BusinessException(Code.PARAMS_ERROR);
        }
        String userAccount = userRegister.getUserAccount();
        String userPassword = userRegister.getUserPassword();
        String checkPassword = userRegister.getCheckPassword();
        String plantId = userRegister.getPlantId();
        long id = userService.userRegister(userAccount, userPassword, checkPassword,plantId);
        return ResultUtils.success(id);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String userName, HttpServletRequest request) {
        //判断当前用户权限
        if (!checkRole(request)){
            throw new BusinessException(Code.PARAM_NULL_ERROR);
        }
        //查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)){
            queryWrapper.select("userName", userName);
        }
        List<User> users = userService.list(queryWrapper);
        //脱敏
        return ResultUtils.success(users.stream().map(user -> {
                    return userService.getSafeUser(user);
                }
        ).collect(Collectors.toList()));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(long id, HttpServletRequest request) {
        //判断当前用户权限
        if (!checkRole(request)){
            throw new BusinessException(Code.NO_AUTH);
        }
        //按照id删除
        if (id < 0) {
            return ResultUtils.error(Code.PARAM_NULL_ERROR);
        }
        return ResultUtils.success(userService.removeById(id));
    }

    /**
     * 检查用户权限
     *
     * @param request session
     * @return 是否为管理员
     */
    public boolean checkRole(HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null || currentUser.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
