create table user
(
    id           bigint auto_increment comment '主键'
        primary key,
    userAccount  varchar(1024)                       null comment '账户',
    userName     varchar(1024)                       null comment '账户名称',
    userUrl      varchar(512)                        null comment '头像',
    gender       tinyint                             null comment '性别 0-女 1-男',
    userPassword varchar(512)                        null comment '密码',
    userStatus   tinyint   default 0                 null comment '状态 0-正常',
    createTime   timestamp default CURRENT_TIMESTAMP null,
    updateTime   timestamp default CURRENT_TIMESTAMP null,
    isDelete     int       default 0                 null comment '是否删除（逻辑删除） 0-正常 1-删除',
    userRole     int       default 0                 not null comment '用户权限 0-普通用户 1-管理员',
    plantId      varchar(512)                        null comment '星球id-用户校验'
)
    comment '用户';