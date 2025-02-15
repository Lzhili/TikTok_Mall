# 启动TC服务

1. 下载v2.2.0的[seata-server二进制包](https://seata.apache.org/zh-cn/download/seata-server/)至本地 (注意是二进制不是源码！)。TC服务是事务协调中心，是一个独立的微服务，需要单独启动。
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

