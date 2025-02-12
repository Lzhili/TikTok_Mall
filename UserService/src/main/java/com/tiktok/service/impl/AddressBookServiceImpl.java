package com.tiktok.service.impl;

import com.tiktok.dto.AddressBookDTO;
import com.tiktok.entity.AddressBook;
import com.tiktok.mapper.AddressBookMapper;
import com.tiktok.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
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

    /**
     * 当前用户新增一个收货地址
     * @param addressBookDTO
     */
    @Override
    public void addAddress(AddressBookDTO addressBookDTO) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookDTO, addressBook);
//        Long userId = BaseContext.getCurrentId();
//        addressBook.setUserId(userId);

        addressBookMapper.addOne(addressBook);
    }

}
