package com.tiktok.filter;

import cn.dev33.satoken.same.SaSameUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Sa-Token 整合 Dubbo Provider端过滤器
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = -10000)
public class DubboProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.info("ProductService: Provider Same-Token invoke");

        // 取出 Same-Token 进行校验
        String sameToken = invocation.getAttachment(SaSameUtil.SAME_TOKEN);
        SaSameUtil.checkToken(sameToken);

        // 取出其他自定义附加数据
        // TenantContext tenantContext = invocation.getAttachment("tenantContext");

        // 开始调用
        return invoker.invoke(invocation);
    }

}