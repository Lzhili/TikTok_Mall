# 应用接入Sentinel控制台

1. 按照 [Sentinel 控制台文档](https://github.com/alibaba/Sentinel/wiki/%E6%8E%A7%E5%88%B6%E5%8F%B0) 安装并启动控制台(可以设置控制台端口为8099)。
```
java -Dserver.port=8099 -Dcsp.sentinel.dashboard.server=localhost:8099 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```

2. dubbo应用引入相关依赖，并给应用添加相关的JVM启动参数

ShoppingCartDubboApp添加:
```
-Dproject.name=ShoppingCart -Dcsp.sentinel.dashboard.server=localhost:8099 -Dcsp.sentinel.api.port=8721
```
OrderDubboApp添加:
```
-Dproject.name=Order -Dcsp.sentinel.dashboard.server=localhost:8099 -Dcsp.sentinel.api.port=8722
```
3. 启动应用，并访问控制台 http://localhost:8099，就能在控制台找到对应的应用了（注意需要访问各个接口才能触发簇点链路，且一些接口可能由于redis缓存导致无法触发簇点链路，可以先删除一下redis缓存）。

最佳实践请参考[官方文档](https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/tasks/rate-limit/sentinel/)

# Provider 请求限流
流控规则：   
我们把查询历史订单列表这个簇点资源的流量限制在了每秒6个，也就是最大QPS为6.

限流测试：  
利用Jemeter做限流测试，我们每秒发出10个请求.<br>
<img src=img/限流Jmeter1.png width="75%" />

结果：<br>
<img src=img/限流测试图.png width="75%" /> 

可以看出这个接口的通过QPS稳定在6附近，而拒绝的QPS在4附近，符合我们的预期。

# Consumer 线程隔离
流控规则：   
我们模拟根据id查询商品的业务A存在500ms的延迟，而购物车服务中的添加购物车业务B需要调用业务A。
为了避免因商品服务出现故障导致购物车服务级联失败，我们可以把购物车业务中添加购物车的部分隔离起来，限制可用的线程资源为5，因此QPS为10.

线程隔离测试：  
限制购物车服务的最大线程数为100，用于线程隔离和熔断降级的测试。然后利用Jemeter测试，每秒发送100个请求：<br>
<img src=img/线程隔离Jmeter1.png width="75%" />  

结果：<br>
<img src=img/限流测试图.png width="75%" />  

进入添加购物车的请求每秒大概在100，而在根据id查询商品时却只剩下每秒10左右，符合我们的预期。
此时如果我们通过页面访问购物车的其它接口，例如查询购物车等，发现其延迟不受影响（如果不设置线程隔离，会显著变慢），这就证明线程隔离起到了作用。

# Fallback
触发限流或熔断后的请求不一定要直接报错，也可以返回一些默认数据或者友好提示，用户体验会更好，
可以编写触发限流或熔断后的处理逻辑fallback。再次测试，发现被限流的请求不再报错，走了fallback逻辑：<br>

<img src=img/fallback.png width="75%" />  

但是未被限流的请求延时依然很高，导致最终的平局响应时间较长（如图，54ms）。

# Consumer 熔断降级
查询商品的RT较高（模拟的500ms），从而导致添加购物车的RT也变的很长。对于商品服务这种不太健康的接口，我们应该停止调用，直接走fallback，避免影响到当前服务。也就是将商品查询接口熔断。
当商品服务接口恢复正常后，再允许调用。这其实就是断路器的工作模式了。
Sentinel中的断路器不仅可以统计某个接口的慢请求比例，还可以统计异常请求比例。当这些比例超出阈值时，就会熔断该接口，即拦截访问该接口的一切请求，降级处理；当该接口恢复正常时，再放行对于该接口的请求。

我们按照慢调用比例来做熔断，配置的含义是：
- RT超过200毫秒的请求调用就是慢调用
- 统计最近1000ms内的最少5次请求，如果慢调用比例不低于0.5，则触发熔断
- 熔断持续时长20s

配置完成后，再次利用Jemeter测试，可以发现：<br>
<img src=img/熔断降级.png width="75%" />  

在一开始一段时间是允许访问的，后来触发熔断后，查询商品服务的接口通过QPS直接为0，所有请求都被熔断了。而添加购物车的本身并没有受到影响。

此时整个添加购物车服务的平均RT影响不大：<br>
<img src=img/熔断降级的平均RT时间.png width="75%" />  