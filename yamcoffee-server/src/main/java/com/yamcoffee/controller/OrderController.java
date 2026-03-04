package com.yamcoffee.controller;

import com.yamcoffee.common.Constants;
import com.yamcoffee.common.PageResult;
import com.yamcoffee.common.Result;
import com.yamcoffee.dto.DirectOrderDTO;
import com.yamcoffee.dto.OrderCreateDTO;
import com.yamcoffee.entity.Order;
import com.yamcoffee.service.OrderService;
import com.yamcoffee.vo.OrderCountVO;
import com.yamcoffee.vo.OrderDetailVO;
import com.yamcoffee.vo.OrderVO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    public Result<Map<String, String>> createOrder(HttpServletRequest request, @Valid @RequestBody OrderCreateDTO dto) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        Order order = orderService.createOrder(userId, dto);
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", order.getOrderNo());
        return Result.success(result);
    }
    
    @PostMapping("/direct")
    public Result<Map<String, String>> createDirectOrder(HttpServletRequest request, @Valid @RequestBody DirectOrderDTO dto) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        Order order = orderService.createDirectOrder(userId, dto);
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", order.getOrderNo());
        return Result.success(result);
    }
    
    @GetMapping("/list")
    public Result<PageResult<OrderVO>> getOrderList(
            HttpServletRequest request,
            @RequestParam(defaultValue = "-1") Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        PageResult<OrderVO> result = orderService.getOrderList(userId, status, pageNum, pageSize);
        return Result.success(result);
    }
    
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable String orderNo, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        OrderDetailVO order = orderService.getOrderDetail(orderNo, userId);
        return Result.success(order);
    }
    
    @PutMapping("/{orderNo}/cancel")
    public Result<Void> cancelOrder(@PathVariable String orderNo, HttpServletRequest request,
                                    @RequestBody(required = false) Map<String, String> body) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        String reason = body != null ? body.get("reason") : null;
        orderService.cancelOrder(orderNo, userId, reason);
        return Result.success();
    }
    
    @PutMapping("/{orderNo}/receive")
    public Result<Void> confirmReceive(@PathVariable String orderNo, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        orderService.confirmReceive(orderNo, userId);
        return Result.success();
    }
    
    @GetMapping("/count")
    public Result<OrderCountVO> getOrderCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        OrderCountVO count = orderService.getOrderCount(userId);
        return Result.success(count);
    }
    
    @PutMapping("/{orderNo}/confirm-pay")
    public Result<Void> confirmPay(@PathVariable String orderNo, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        orderService.confirmPay(orderNo, userId);
        return Result.success();
    }
    
    @PutMapping("/{orderNo}/confirm-receipt-payment")
    public Result<Void> confirmReceiptPayment(@PathVariable String orderNo, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        orderService.confirmReceiptPayment(orderNo, userId);
        return Result.success();
    }
}
