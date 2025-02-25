package com.tiktok.mapper;

import com.tiktok.entity.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface OperateLogMapper {

    /**
     *  插入日志数据
     */
    @Insert("insert into `tiktok-mall`.operate_log (operate_user, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateUser}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insert(OperateLog log);
}
