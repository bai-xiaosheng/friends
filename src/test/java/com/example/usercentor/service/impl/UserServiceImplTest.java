package com.example.usercentor.service.impl;

import com.example.usercentor.modal.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceImplTest {
//    @Resource
    @Autowired
    private UserServiceImpl userService;

    @Test
    void stringEquleTest(){
        char[] chars1=new char[]{'a','b','c'};
        String a=new String(chars1);
        char[] chars2=new char[]{'a','b','c'};
        String b=new String(chars2);
        System.out.println("a == b : "+ (a == b));
        System.out.println("a.equals(b) : " + a.equals(b));
    }


    @Test
    void userRegester(){
        String userAccount = "";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String plantId = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, plantId);
        Assertions.assertEquals(-1,result);
        userAccount = "dogyupi";
        userPassword = "1234";
        result = userService.userRegister(userAccount, userPassword, checkPassword, plantId);
        Assertions.assertEquals(-1,result);
        userAccount = "dogyupi@";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, plantId);
        Assertions.assertEquals(-1,result);
        userAccount = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, plantId);
        Assertions.assertEquals(-1,result);
        userAccount = "dogyupi";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, plantId);
        Assertions.assertEquals(-1,result);
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, plantId);
        Assertions.assertTrue(result > 0);

    }
    @Test
    void AddUser(){
        User user = new User();
        user.setUserAccount("123456");
        user.setUserName("12365");
        user.setUserUrl("https://pic3.zhimg.com/v2-f14591e264708123e97ce9b14f8e7422_r.jpg");
        user.setGender(1);
        user.setUserPassword("1234546");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertEquals(true,result);
    }
}