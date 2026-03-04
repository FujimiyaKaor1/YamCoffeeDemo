package com.yamcoffee.controller;

import com.yamcoffee.common.Result;
import com.yamcoffee.entity.PaymentConfig;
import com.yamcoffee.service.PaymentConfigService;
import com.yamcoffee.vo.PaymentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    
    private final PaymentConfigService paymentConfigService;
    
    public PaymentController(PaymentConfigService paymentConfigService) {
        this.paymentConfigService = paymentConfigService;
    }
    
    @GetMapping("/qrcode")
    public Result<PaymentVO> getQrCode() {
        PaymentConfig config = paymentConfigService.getActiveConfig();
        PaymentVO vo = new PaymentVO();
        if (config != null) {
            BeanUtils.copyProperties(config, vo);
        }
        return Result.success(vo);
    }
    
    @PutMapping("/config")
    public Result<Void> updateConfig(@RequestBody PaymentConfig config) {
        config.setStatus(1);
        paymentConfigService.updateConfig(config);
        return Result.success();
    }
    
    @GetMapping("/config")
    public Result<PaymentVO> getConfig() {
        PaymentConfig config = paymentConfigService.getActiveConfig();
        PaymentVO vo = new PaymentVO();
        if (config != null) {
            BeanUtils.copyProperties(config, vo);
        }
        return Result.success(vo);
    }
}
