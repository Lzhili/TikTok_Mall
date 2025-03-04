package com.tiktok.mapper;

import com.tiktok.annotation.AutoFill;
import com.tiktok.entity.User;
import com.tiktok.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     *根据邮箱查询用户
     * @param email
     * @return
     */
    @Select("select * from `tiktok-mall`.user where email = #{email}")
    User getByEmail(String email);

    /**
     * 注册，插入用户
     * @param user
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(User user);

    @Select("select * from `tiktok-mall`.user where id = #{id}")
    User getById(Long id);

    @Update("update `tiktok-mall`.user set role = #{role} where id = #{id}")
    void updateRoleById(Long id, String role);

    @Select("select role from `tiktok-mall`.user where id = #{id}")
    String getRoleById(Long id);
}
