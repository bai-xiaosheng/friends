# 使用Java 8基础镜像
FROM java:8
LABEL authors="mabh"

# 设置时区为Asia/Shanghai，可以根据需要更改
ENV TIME_ZONE=Asia/Shanghai

# 更新时区
RUN ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

# 设置容器内的工作目录
WORKDIR /app

# 将构建好的Spring Boot应用jar文件复制到镜像中
COPY ./${project.build.finalName}.jar /app/

COPY ./application.yml /app/
COPY ./application-${activatedProperties}.yml /app/

# 暴露端口
EXPOSE 8080


# 设置Java虚拟机初始内存和最大内存
#-server: 启用Java HotSpot虚拟机的服务器模式，该模式针对长时间运行的应用程序进行了优化以提高性能。
#-Xms512m: 设置Java堆的初始大小为512兆字节。
#-Xmx1024m: 设置Java堆的最大大小为1024兆字节。
#-XX:+UseG1GC: 启用G1垃圾收集器。G1（Garbage-First）是一种相对新的垃圾收集器，旨在提供更可预测的停顿时间和更好的性能。
#-XX:+HeapDumpOnOutOfMemoryError: 在发生内存溢出错误时生成堆转储文件。这对于分析内存问题非常有用。
#-XX:HeapDumpPath=/dumps/oom_dump.hprof: 指定内存转储文件。在这个例子中，堆转储文件将被写入 /dumps/oom_dump.hprof 目录文件中。
ENV JAVA_OPTS="-server -Xms512m -Xmx1024m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/dumps/oom_dump.hprof"

ENV APP_ENV="--spring.profiles.active=${activatedProperties}"
# 创建内存转储文件和日志文件存储目录
RUN mkdir /app/dumps
RUN mkdir /app/logs

# 设置容器启动时执行的命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dlogging.file=/app/logs/application.log -jar /app/${project.build.finalName}.jar $APP_ENV"]



#FROM maven:3.5-jdk-8-apline as builder
#
### WORKDIR 设置工作目录，也就是进入容器的默认目录
#WORKDIR /app
##COPY pom.xml .
##COPY src ./src
#
##RUN mvn package -DskipTests
#
#
#
### EXPOSE 容器的暴露端口
#EXPOSE 8080
#
#
#
#COPY usercenter-0.0.1-SNAPSHOT.jar usercenter.jar
#
##COPY application.yml prod.yml
#
#
#CMD ["java -jar /usercenter.jar --spring.profiles.active=prod"]
##CMD ["echo","hello world"]
#
##ENTRYPOINT ["java","-jar","/spring-cas.jar","--spring.config.location=/prod.yml"]

