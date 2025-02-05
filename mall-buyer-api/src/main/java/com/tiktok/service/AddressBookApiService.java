package com.tiktok.service;

import com.tiktok.context.BaseContext;
import com.tiktok.entity.AddressBook;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookApiService {

    @DubboReference
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    public List<AddressBook> list() {
        return addressBookService.list(BaseContext.getCurrentId());
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    public AddressBook getById(Long id) {
        return addressBookService.getById(id);
    }
}
