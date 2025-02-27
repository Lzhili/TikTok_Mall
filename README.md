# TikTok_Mall 抖音电商项目
- 该项目为前后端分离项目的后端部分，前端项目`vue-mall`地址: [传送门](https://github.com/lzzlqwe/vue-mall)<br>
- 前端主界面效果图如下：<br>
<br><img src=docs/img/homeView.png width="90%" />

## 项目背景
该项目旨在构建一个基于Java和Dubbo的“简易版”微服务抖音商城系统。通过模块化设计，将不同的业务功能拆分为独立的服务，提升系统的可维护性和扩展性。

## 项目目标
- 构建一个功能完备且可扩展的微服务商城系统。
- 各服务模块之间通过Dubbo进行高效通信，保证系统的稳定性和高可用性。
- 提供友好的用户界面，用户可以通过浏览器访问前端页面并进行购物操作。

## 服务模块概述
- `TestService`: 用于测试dubbo功能。
- `UserService`: 用户服务，提供用户注册、登录等业务功能。
- `AuthService`: 发放token功能等业务功能。
- `ProductService`: 商品服务，提供商品查询、获取商品详细信息等业务功能。
- `ShoppingCartService`: 购物车服务，提供购物车查询、添加商品等业务功能。
- `OrderService`: 订单服务，提供订单创建、查询等业务功能。
- `PaymentService`: 提供支付功能等业务功能。

## 项目配置
### 本项目的开发环境
- JDK17 <br>
- SpringBoot 3.2.0 <br>
- Apache Dubbo 3.2.2 <br>
- Sa-Token 1.39.0 <br>

### 配置文件
在每个服务的 `src/main/resources`目录下，创建配置文件`application-dev.yml`，请根据实际环境调整配置信息。下面是示例：
```
# spring jackson
spring:
  jackson:
    time-zone: GMT+8

tiktok:
  datasource: # 数据库mysql信息
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost
    port: 3306
    database: tiktok-mall
    username: root
    password: 123456
  redis: #redis配置
    host: localhost
    port: 6379
    password:
    database: 0
  dubbo: #dubbo配置
    registry:
      address: zookeeper://127.0.0.1:2181 #zookeeper配置
    protocol:
      name: tri  # triple协议配置
      port: 20884 # 协议端口号配置（不同服务的端口号需不同，可以自己设置）
```

## 准备工作
1. 启动 redis。
2. 启动zookeeper注册中心服务([详见文档](https://blog.csdn.net/tttzzzqqq2018/article/details/132093374))
```
zkServer # 假设已经配好环境变量
```
3. （可选）启动Sentinel控制台，用于服务监控和保护([详见文档](docs/使用Sentinel进行服务保护.md))
```
java -Dserver.port=8099 -Dcsp.sentinel.dashboard.server=localhost:8099 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```
4. 启动Seata，用于提供分布式事务解决方案([详见文档](docs/Seata提供分布式事务解决方案.md))
```
seata-server.bat -p 8091 -h 127.0.0.1 -m file  #在安装目录的bin路径下执行该命令
```
5. 启动RabbitMQ。请先安装RabbitMQ，参考[安装文档](https://blog.csdn.net/qq_39915083/article/details/107034747)，并安装RabbitMQ的延时消息插件<br>
```
net start RabbitMQ   #以管理员身份启动cmd
```
补充：RabbitMQ延时消息插件 <br>
到[插件页面](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases)下载对应版本的.ez文件（由于我们安装的MQ是4.0.6版本，因此这里下载4.0.2版本，具体视个人而定）。将插件复制到 RabbitMQ的plugin文件夹中，然后通过运行以下命令来启用它：
```
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

## 服务启动顺序
以下是provider服务启动的顺序：
1. 启动`TestService`
2. 启动`UserService`
3. 启动`AuthService`
4. 启动`ProductService`
5. 启动`ShoppingCartService`
6. 启动`OrderService`
7. 启动`PaymentService`

最后再启动`mall-buyer-api`


## 相关安装
### zookeeper安装
- [3.9.1版本安装包下载](https://archive.apache.org/dist/zookeeper/zookeeper-3.9.1/) <br>
- [参考链接](https://blog.csdn.net/tttzzzqqq2018/article/details/132093374) <br>

### PrettyZoo——zookeeper可视化工具
- [中文文档](https://github.com/vran-dev/PrettyZoo/blob/master/README_CN.md)<br>
- [下载地址](https://github.com/vran-dev/PrettyZoo/releases)<br>

## 其他相关文档
- 接口测试文档：[传送门](docs/api接口测试.md)
- 数据库建表语句：[传送门](docs/Mysql数据库文档.md)
- 服务保护文档：[传送门](docs/使用Sentinel进行服务保护.md)
- 分布式事务文档：[传送门](docs/Seata提供分布式事务解决方案.md)




