package com.tiktok.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.tiktok.annotation.Log;
import com.tiktok.dto.AddressBookDTO;
import com.tiktok.entity.AddressBook;
import com.tiktok.result.Result;
import com.tiktok.service.AddressBookApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "地址簿管理接口", description = "地址簿相关接口")
@RestController
@Slf4j
@RequestMapping(value = "/buyer/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookApiService addressBookApiService;

    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        log.info("查询当前登录用户的所有地址信息");
        List<AddressBook> list = addressBookApiService.list();
        return Result.success(list);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookApiService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 当前用户添加一个收货地址
     * @param addressBookDTO
     * @return
     */
    @Log
    @PostMapping
    @Operation(summary = "当前用户添加一个收货地址")
    public Result<String> addAddress(@RequestBody AddressBookDTO addressBookDTO) {
        addressBookApiService.addAddress(addressBookDTO);
        return Result.success("添加地址成功！");
    }
}
