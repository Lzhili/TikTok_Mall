package com.tiktok.mapper;

import com.tiktok.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 根据用户id查询所有地址
     * @param userId
     * @return
     */
    List<AddressBook> list(Long userId);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Select("select * from `tiktok-mall`.address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 当前用户插入一条收货地址
     * @param addressBook
     */
    @Insert("insert into `tiktok-mall`.address_book (user_id, username, email, detail, city, province, country, label, zip_code) " +
            "values(#{userId}, #{username}, #{email}, #{detail}, #{city}, #{province}, #{country}, #{label}, #{zipCode})")
    void addOne(AddressBook addressBook);
}
