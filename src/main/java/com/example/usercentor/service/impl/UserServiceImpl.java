package com.example.usercentor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercentor.common.Code;
import com.example.usercentor.exception.BusinessException;
import com.example.usercentor.mapper.UserMapper;
import com.example.usercentor.modal.domain.User;
import com.example.usercentor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.usercentor.constant.Constant.USER_LOGIN_STATE;

/**
 * @author BDS
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-05-05 15:35:01
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
            implements UserService {
    @Resource
    private UserMapper userMapper;
    public static final String SALT = "YUPI";


    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 用户账号
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String plantId) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, plantId)) {
            throw new BusinessException(Code.PARAMS_ERROR,"输入参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(Code.PARAMS_ERROR,"账户长度小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(Code.PARAMS_ERROR,"密码长度小于8位");
        }
        //账户不能包含特殊符号
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(Code.PARAMS_ERROR,"账户包含特殊字符");
        }
        //两次密码校验
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(Code.PARAMS_ERROR,"两次密码不一致");
        }
        //星球id不能重复
        QueryWrapper<User> queryWrapperId = new QueryWrapper<>();
        queryWrapperId.eq("plantId", plantId);
        long countId = userMapper.selectCount(queryWrapperId);
        if (countId > 0) {
            throw new BusinessException(Code.PARAMS_ERROR,"当前星球账户已注册");
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(Code.PARAMS_ERROR,"当前账户名已注册");
        }
        //2.密码加密
        String savePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(savePassword);
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(Code.SAVE_ERROR,"请重试");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request 客户端请求
     * @return 用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(Code.PARAMS_ERROR,"输入参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(Code.PARAMS_ERROR,"账户长度小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(Code.PARAMS_ERROR,"密码长度小于8位");
        }
        //账户不能包含特殊符号
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(Code.PARAMS_ERROR,"账户包含特殊字符");
        }

        //2.密码加密
        String savePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //检查用户账号信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", savePassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot math userPassword");
            throw new BusinessException(Code.PARAM_NULL_ERROR,"数据库中不包含该账户");
        }
        //3.用户信息脱敏
        User safeUser = getSafeUser(user);
        //4.记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);
        //返回脱敏后的用户信息
        return safeUser;

    }
    @Override
    public User getSafeUser(User originUser){
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setUserName(originUser.getUserName());
        safeUser.setUserUrl(originUser.getUserUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setPlantId(originUser.getPlantId());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setUserRole(originUser.getUserRole());
        return safeUser;
    }

    @Override
    public int loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




