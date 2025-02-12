package com.tiktok.service;

import com.tiktok.dto.AddressBookDTO;
import com.tiktok.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    List<AddressBook> list(Long userId);

    AddressBook getById(Long id);

    void addAddress(AddressBookDTO addressBookDTO);
}
