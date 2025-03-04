package com.tiktok.service.impl;


import com.tiktok.constant.MessageConstant;
import com.tiktok.dto.UserLoginDTO;
import com.tiktok.dto.UserRegisterDTO;
import com.tiktok.entity.User;
import com.tiktok.exception.AccountNotFoundException;
import com.tiktok.exception.IllegalEmailException;
import com.tiktok.exception.PasswordConfirmErrorException;
import com.tiktok.exception.PasswordErrorException;
import com.tiktok.mapper.UserMapper;
import com.tiktok.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;


@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();

        //根据email查询用户
        User user = userMapper.getByEmail(email);

        //2、处理各种异常情况（账号不存在、密码不对）
        if (user == null) {
            //账号不存在
            log.info("账号不存在");
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            log.info("密码错误");
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return user;

    }

    @Override
    public Long register(UserRegisterDTO userRegisterDTO) {
        // 检查 email 是否包含 '@' 符号
        String email = userRegisterDTO.getEmail();
        if (!email.contains("@")) {
            throw new IllegalEmailException(MessageConstant.ILLEGAL_EMAIL_ERROR);
        }

        //注册的密码二次验证不一致，则抛出异常
        String password = userRegisterDTO.getPassword();
        String confirm_password = userRegisterDTO.getConfirm_password();
        if (!password.equals(confirm_password)){
            throw new PasswordConfirmErrorException(MessageConstant.PASSWORD_CONFIRM_ERROR);
        }

        // 注册的密码二次比对通过
        //复制两个对象之间名称和类型匹配的属性
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);

        //将明文密码转换为密文密码
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));

        //设置创建时间和更新时间（优化为AOP）

        //注册，即相当于插入数据
        userMapper.insert(user);

        //取得插入数据库后对应的id，返回
        return user.getId();
    }

    @Override
    public User getById(Long id) {
        //根据 id 查询用户
        User user = userMapper.getById(id);

        // 返回实体对象
        return user;
    }

    @Override
    public void updateRoleById(Long id, String role) {
        userMapper.updateRoleById(id, role);
    }

    @Override
    public String getUserRoleById(Long id) {
        return userMapper.getRoleById(id);
    }
}
