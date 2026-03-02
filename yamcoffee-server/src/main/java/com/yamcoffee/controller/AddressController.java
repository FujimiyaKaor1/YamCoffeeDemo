package com.yamcoffee.controller;

import com.yamcoffee.common.Constants;
import com.yamcoffee.common.Result;
import com.yamcoffee.dto.AddressDTO;
import com.yamcoffee.entity.Address;
import com.yamcoffee.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    
    private final AddressService addressService;
    
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }
    
    @GetMapping("/list")
    public Result<List<Address>> getAddressList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        List<Address> addresses = addressService.getAddressList(userId);
        return Result.success(addresses);
    }
    
    @GetMapping("/{id}")
    public Result<Address> getAddressById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        Address address = addressService.getAddressById(id, userId);
        return Result.success(address);
    }
    
    @PostMapping
    public Result<Address> addAddress(HttpServletRequest request, @Valid @RequestBody AddressDTO dto) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        Address address = addressService.addAddress(userId, dto);
        return Result.success(address);
    }
    
    @PutMapping("/{id}")
    public Result<Address> updateAddress(@PathVariable Long id, HttpServletRequest request,
                                         @Valid @RequestBody AddressDTO dto) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        Address address = addressService.updateAddress(id, userId, dto);
        return Result.success(address);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        addressService.deleteAddress(id, userId);
        return Result.success();
    }
    
    @PutMapping("/{id}/default")
    public Result<Void> setDefaultAddress(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        addressService.setDefaultAddress(id, userId);
        return Result.success();
    }
}
