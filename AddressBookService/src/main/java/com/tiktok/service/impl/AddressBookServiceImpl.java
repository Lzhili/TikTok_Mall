package com.tiktok.service.impl;

import com.tiktok.entity.AddressBook;
import com.tiktok.mapper.AddressBookMapper;
import com.tiktok.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = AddressBookService.class)
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 查询当前登录用户的所有地址信息
     * @param userId
     * @return
     */
    @Override
    public List<AddressBook> list(Long userId) {
        return addressBookMapper.list(userId);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }
}
