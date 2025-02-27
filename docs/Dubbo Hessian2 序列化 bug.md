# Dubbo Hessian2 序列化 bug
启动服务时，jvm 环境加上一下参数即可：
```
--add-opens java.base/java.math=ALL-UNNAMED
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.nio=ALL-UNNAMED
--add-opens java.base/sun.nio.ch=ALL-UNNAMED
```
参考：
1. https://blog.csdn.net/tczzstc/article/details/135997960#:~:text=java.lang.reflect.InaccessibleObjectException:
2. https://blog.csdn.net/weixin_55360845/article/details/124776323