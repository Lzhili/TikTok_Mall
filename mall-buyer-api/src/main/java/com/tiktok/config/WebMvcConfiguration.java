package com.tiktok.config;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.tiktok.context.BaseContext;
import com.tiktok.interceptor.JwtTokenUserInterceptor;
import com.tiktok.json.JacksonObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.Objects;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

//    @Autowired
//    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
//        registry.addInterceptor(jwtTokenUserInterceptor)
//                .addPathPatterns("/buyer/**")
//                .excludePathPatterns("/buyer/user/login")
//                .excludePathPatterns("/buyer/user/register")
//                .excludePathPatterns("/buyer/test");
        // 注册 Sa-Token 拦截器
//        registry.addInterceptor(new SaInterceptor(handle -> {
//                    // 在 SaInterceptor 的逻辑执行之前，设置当前用户 ID
//                    if (StpUtil.isLogin()) {
//                        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
//                        BaseContext.setCurrentId(userId); // 设置当前用户 ID
//                        log.info("当前用户id：{}", userId);
//                    }
//                }))
//                .addPathPatterns("/buyer/**")
//                .excludePathPatterns("/buyer/user/login")
//                .excludePathPatterns("/buyer/user/register")
//                .excludePathPatterns("/buyer/test");
        // Sa-Token 全局鉴权拦截器
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 1. 校验是否登录
            SaRouter.match("/buyer/**") // 匹配所有 buyer 路径
                    .notMatch("/buyer/user/login", "/buyer/user/register", "/buyer/logout", "/buyer/test") // 排除白名单
                    .check(r -> StpUtil.checkLogin());

            if (StpUtil.isLogin()) {
                Long userId = Long.valueOf(StpUtil.getLoginId().toString());
                BaseContext.setCurrentId(userId); // 设置当前用户 ID
                log.info("当前用户id：{}", userId);
            }

            // 2. 逐个匹配路径并校验权限
            SaRouter.match("/buyer/addressBook/**").check(r -> StpUtil.checkPermission("buyer:addressBook:*"));
            SaRouter.match("/buyer/category/**").check(r -> StpUtil.checkPermission("buyer:category:*"));
            SaRouter.match("/buyer/order/**").check(r -> StpUtil.checkPermission("buyer:order:*"));
            SaRouter.match("/buyer/payment/**").check(r -> StpUtil.checkPermission("buyer:payment:*"));
            SaRouter.match("/buyer/product/page").check(r -> StpUtil.checkPermission("buyer:product:page"));
            SaRouter.match(SaHttpMethod.GET).match("/buyer/product/{id}").check(r -> StpUtil.checkPermission("buyer:product:getId")); // 匹配 /buyer/product/{id}
            SaRouter.match("/buyer/shoppingCart/**").check(r -> StpUtil.checkPermission("buyer:shoppingCart:*"));
        })).addPathPatterns("/**"); // 拦截所有路径
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public OpenAPI openAPI(){
        log.info("准备生成接口文档...");
        return new OpenAPI().info(new Info()
            .title("抖音电商项目接口文档")
            .description("抖音电商项目接口文档")
            .version("1.0"));
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
        // 添加静态资源映射规则
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展spring MVC框架的消息转换器,对后端对后端返回前端的数据进行统一处理
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //为消息转换器设置一个对象转换器，底层使用Jackson将Java对象转为json
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转化器加入到容器中
        converters.add(converters.size()-1, converter);
    }
}
