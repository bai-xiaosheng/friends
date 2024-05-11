package com.example.usercentor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usercentor.modal.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author BDS
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-05 15:35:01
*/
public interface UserService extends IService<User>{

    long userRegister(String userAccount, String userPassword, String checkPassword, String plantId);


    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafeUser(User user);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int loginOut(HttpServletRequest request);
}
