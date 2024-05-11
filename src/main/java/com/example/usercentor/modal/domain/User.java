package com.example.usercentor.modal.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账户
     */
    private String userAccount;

    /**
     * 账户名称
     */
    private String userName;

    /**
     * 头像
     */
    private String userUrl;

    /**
     * 性别 0-女 1-男
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 状态 0-正常
     */
    private Integer userStatus;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除（逻辑删除） 0-正常 1-删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户权限 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 星球id-用户校验
     */
    private Integer plantId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}