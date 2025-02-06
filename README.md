# TikTok_Mall 抖音电商项目
- 该项目为前后端分离项目的后端部分，前端项目`vue-mall`地址: [传送门](https://github.com/lzzlqwe/vue-mall)<br>
- 前端主界面效果图如下：
<br><img src=docs/img/homeView.png width="75%" />

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
- `CategoryService`: 提供商品分类等业务功能。
- `AddressBookService`: 查询当前用户保存的地址等业务功能。
- `ProductService`: 商品服务，提供商品查询、获取商品详细信息等业务功能。
- `ShoppingCartService`: 购物车服务，提供购物车查询、添加商品等业务功能。
- `OrderService`: 订单服务，提供订单创建、查询等业务功能。

## 项目配置
### 本项目的开发环境
- JDK17 <br>
- SpringBoot 3.2 <br>
- Apache Dubbo 3.3 <br>

### 配置文件
在每个服务的 `src/main/resources`目录下，创建配置文件`application-dev.yml`，请根据实际环境调整配置信息

## 服务启动顺序
以下是provider服务启动的顺序：
1. 启动`TestService`
2. 启动`UserService`
3. 启动`AuthService`
4. 启动`CategoryService`
5. 启动`AddressBookService`
6. 启动`ProductService`
7. 启动`ShoppingCartService`
8. 启动`OrderService`

最后再启动：
9. 启动`mall-buyer-api`


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




