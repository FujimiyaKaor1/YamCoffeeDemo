package com.yamcoffee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yamcoffee.entity.PaymentConfig;
import com.yamcoffee.mapper.PaymentConfigMapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentConfigService {
    
    private final PaymentConfigMapper paymentConfigMapper;
    
    public PaymentConfigService(PaymentConfigMapper paymentConfigMapper) {
        this.paymentConfigMapper = paymentConfigMapper;
    }
    
    public PaymentConfig getActiveConfig() {
        LambdaQueryWrapper<PaymentConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentConfig::getStatus, 1)
                .orderByDesc(PaymentConfig::getUpdateTime)
                .last("LIMIT 1");
        return paymentConfigMapper.selectOne(wrapper);
    }
    
    public boolean updateConfig(PaymentConfig config) {
        if (config.getId() != null) {
            return paymentConfigMapper.updateById(config) > 0;
        } else {
            return paymentConfigMapper.insert(config) > 0;
        }
    }
}
