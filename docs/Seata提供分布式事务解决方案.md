# Seata部署指南

Seata分TC、TM和RM三个角色，TC(Server端)为单独服务端部署，TM和RM(Client端)由业务系统集成。

## Seata服务端

1. 下载v2.2.0的[seata-server二进制包](https://seata.apache.org/zh-cn/download/seata-server/) 至本地 (注意是二进制不是源码！)。TC服务是事务协调中心，是一个独立的微服务，需要单独启动。
2. 解压后进入`seata-server\bin`目录运行下面的命令(window系统，设置服务端口为8091)

```
seata-server.bat -p 8091 -h 127.0.0.1 -m file
```
详情配置见[官方文档](https://seata.apache.org/zh-cn/docs/user/quickstart)
```
Usage: sh seata-server.sh(for linux and mac) or cmd seata-server.bat(for windows) [options]
  Options:
    --host, -h
      The address is expose to registration center and other service can access seata-server via this ip
      Default: 0.0.0.0
    --port, -p
      The port to listen.
      Default: 8091
    --storeMode, -m
      log store mode : file、db
      Default: file
    --help

e.g.

sh seata-server.sh -p 8091 -h 127.0.0.1 -m file
```

## Seata客户端

### 集成Seata客户端

参考[官方示例](https://github.com/apache/incubator-seata-samples/tree/master/at-sample) 完成Seata客户端的集成。主要包括：
1. 引入Seata依赖
2. 配置application.yml，用于连接Seata服务端
3. 在全局事务使用@GlobalTransactional

### 创建UNDO_LOG表
在实际应用中，每个dubbo服务应该有各自对应的数据库。 但是，为了简单起见，我们的项目只创建一个数据库并配置各个dubbo服务所对应的数据源。

我们使用分布式事务的AT模式，一般需要为每个数据库创建一个UNDO_LOG表，用于记录快照来回滚信息。这里同样为了简单起见，我们只在一个数据库中创建UNDO_LOG表：
```
-- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

