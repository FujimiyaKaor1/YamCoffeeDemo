package com.yamcoffee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yamcoffee.common.Constants;
import com.yamcoffee.common.ResultCode;
import com.yamcoffee.dto.AddressDTO;
import com.yamcoffee.entity.Address;
import com.yamcoffee.exception.BusinessException;
import com.yamcoffee.mapper.AddressMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {
    
    private final AddressMapper addressMapper;
    
    public AddressService(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }
    
    public List<Address> getAddressList(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime);
        return addressMapper.selectList(wrapper);
    }
    
    public Address getAddressById(Long id, Long userId) {
        Address address = addressMapper.selectById(id);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_EXIST);
        }
        return address;
    }
    
    @Transactional
    public Address addAddress(Long userId, AddressDTO dto) {
        Long count = addressMapper.selectCount(
                new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId)
        );
        if (count >= Constants.ADDRESS_MAX_COUNT) {
            throw new BusinessException(ResultCode.ADDRESS_LIMIT_EXCEEDED);
        }
        
        Address address = new Address();
        BeanUtils.copyProperties(dto, address);
        address.setUserId(userId);
        
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }
        
        addressMapper.insert(address);
        return address;
    }
    
    @Transactional
    public Address updateAddress(Long id, Long userId, AddressDTO dto) {
        Address address = getAddressById(id, userId);
        BeanUtils.copyProperties(dto, address);
        
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }
        
        addressMapper.updateById(address);
        return address;
    }
    
    @Transactional
    public void deleteAddress(Long id, Long userId) {
        Address address = getAddressById(id, userId);
        addressMapper.deleteById(address.getId());
    }
    
    @Transactional
    public void setDefaultAddress(Long id, Long userId) {
        Address address = getAddressById(id, userId);
        clearDefaultAddress(userId);
        
        address.setIsDefault(1);
        addressMapper.updateById(address);
    }
    
    private void clearDefaultAddress(Long userId) {
        LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Address::getUserId, userId)
                .set(Address::getIsDefault, 0);
        addressMapper.update(null, updateWrapper);
    }
    
    public Address getDefaultAddress(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, 1);
        return addressMapper.selectOne(wrapper);
    }
}
