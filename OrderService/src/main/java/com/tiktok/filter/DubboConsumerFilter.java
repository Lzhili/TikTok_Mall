package com.tiktok.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import cn.dev33.satoken.same.SaSameUtil;

/**
 * Sa-Token 整合 Dubbo Consumer端过滤器
 */
@Slf4j
@Activate(group = {CommonConstants.CONSUMER}, order = -10000)
public class DubboConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.info("OrderService: Consumer Same-Token invoke");

        // 追加 Same-Token 参数
        RpcContext.getContext().setAttachment(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());

        // 如果有其他自定义附加数据，如租户
        // RpcContext.getContext().setAttachment("tenantContext", tenantContext);

        // 开始调用
        return invoker.invoke(invocation);
    }

}

