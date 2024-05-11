package com.example.usercentor;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com/example/usercentor/mapper")
//@ComponentScan({"com.example.usercentor"})
public class UsercentorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsercentorApplication.class, args);
    }

}
