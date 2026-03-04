package com.yamcoffee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yamcoffee.common.Constants;
import com.yamcoffee.common.PageResult;
import com.yamcoffee.common.ResultCode;
import com.yamcoffee.dto.DirectOrderDTO;
import com.yamcoffee.dto.OrderCreateDTO;
import com.yamcoffee.entity.*;
import com.yamcoffee.exception.BusinessException;
import com.yamcoffee.mapper.OrderItemMapper;
import com.yamcoffee.mapper.OrderMapper;
import com.yamcoffee.utils.OrderNoGenerator;
import com.yamcoffee.vo.OrderCountVO;
import com.yamcoffee.vo.OrderDetailVO;
import com.yamcoffee.vo.OrderItemVO;
import com.yamcoffee.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartService cartService;
    private final AddressService addressService;
    private final ProductService productService;
    
    public OrderService(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                        CartService cartService, AddressService addressService,
                        ProductService productService) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartService = cartService;
        this.addressService = addressService;
        this.productService = productService;
    }
    
    @Transactional
    public Order createDirectOrder(Long userId, DirectOrderDTO dto) {
        Address address = addressService.getAddressById(dto.getAddressId(), userId);
        
        Product product = productService.getProductEntityById(dto.getProductId());
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXIST, "商品不存在或已下架");
        }
        if (product.getStock() < dto.getQuantity()) {
            throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH, "商品库存不足");
        }
        
        BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(dto.getQuantity()));
        
        String orderNo = OrderNoGenerator.generate();
        
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverAddress(address.getDetailAddress());
        order.setTotalAmount(totalAmount);
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(totalAmount);
        order.setStatus(Constants.ORDER_STATUS_PENDING_PAYMENT);
        order.setPayStatus(Constants.PAY_STATUS_UNPAID);
        order.setDeliveryType(1);
        order.setUserNote(dto.getUserNote());
        order.setEstimatedArrivalTime(LocalDateTime.now().plusDays(3));
        
        orderMapper.insert(order);
        
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(dto.getProductId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setProductPrice(product.getPrice());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setTotalAmount(totalAmount);
        
        orderItemMapper.insert(orderItem);
        
        productService.decreaseStock(dto.getProductId(), dto.getQuantity());
        
        return order;
    }
    
    @Transactional
    public Order createOrder(Long userId, OrderCreateDTO dto) {
        Address address = addressService.getAddressById(dto.getAddressId(), userId);
        
        List<Cart> cartItems = cartService.getSelectedCartItems(userId, dto.getCartItemIds());
        if (cartItems.isEmpty()) {
            throw new BusinessException(ResultCode.CART_EMPTY);
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : cartItems) {
            Product product = productService.getProductById(cart.getProductId()) != null 
                    ? convertToProduct(productService.getProductById(cart.getProductId())) 
                    : null;
            if (product == null || product.getStatus() == 0) {
                throw new BusinessException(ResultCode.PRODUCT_NOT_EXIST, "商品[" + cart.getProductId() + "]不存在或已下架");
            }
            if (product.getStock() < cart.getQuantity()) {
                throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH, "商品[" + product.getName() + "]库存不足");
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
        }
        
        String orderNo = OrderNoGenerator.generate();
        
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverAddress(address.getDetailAddress());
        order.setTotalAmount(totalAmount);
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(totalAmount);
        order.setStatus(Constants.ORDER_STATUS_PENDING_PAYMENT);
        order.setPayStatus(Constants.PAY_STATUS_UNPAID);
        order.setDeliveryType(1);
        order.setUserNote(dto.getUserNote());
        order.setEstimatedArrivalTime(LocalDateTime.now().plusDays(3));
        
        orderMapper.insert(order);
        
        for (Cart cart : cartItems) {
            Product product = convertToProduct(productService.getProductById(cart.getProductId()));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setOrderNo(orderNo);
            orderItem.setProductId(cart.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalAmount(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            
            orderItemMapper.insert(orderItem);
        }
        
        cartService.deleteSelectedCartItems(userId, dto.getCartItemIds());
        
        return order;
    }
    
    public PageResult<OrderVO> getOrderList(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        
        if (status != null && status >= 0) {
            wrapper.eq(Order::getStatus, status);
        }
        
        wrapper.orderByDesc(Order::getCreateTime);
        
        Page<Order> page = new Page<>(pageNum, pageSize);
        Page<Order> result = orderMapper.selectPage(page, wrapper);
        
        List<OrderVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageResult<>(result.getTotal(), pageNum, pageSize, voList);
    }
    
    public OrderDetailVO getOrderDetail(String orderNo, Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId);
        Order order = orderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXIST);
        }
        
        return convertToDetailVO(order);
    }
    
    @Transactional
    public void cancelOrder(String orderNo, Long userId, String reason) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId);
        Order order = orderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXIST);
        }
        
        if (order.getStatus() != Constants.ORDER_STATUS_PENDING_PAYMENT) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "只能取消待付款订单");
        }
        
        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason(reason);
        orderMapper.updateById(order);
    }
    
    @Transactional
    public void confirmReceive(String orderNo, Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId);
        Order order = orderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXIST);
        }
        
        if (order.getStatus() != Constants.ORDER_STATUS_PENDING_RECEIPT) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "只能确认待收货订单");
        }
        
        order.setStatus(Constants.ORDER_STATUS_COMPLETED);
        order.setReceiveTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }
    
    @Transactional
    public void confirmPay(String orderNo, Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId);
        Order order = orderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXIST);
        }
        
        if (order.getStatus() != Constants.ORDER_STATUS_PENDING_PAYMENT) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "只能确认待付款订单");
        }
        
        order.setStatus(Constants.ORDER_STATUS_PAID_PENDING);
        order.setUserPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }
    
    @Transactional
    public void confirmReceiptPayment(String orderNo, Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXIST);
        }
        
        if (order.getStatus() != Constants.ORDER_STATUS_PAID_PENDING) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "只能确认已支付待确认的订单");
        }
        
        order.setStatus(Constants.ORDER_STATUS_PENDING_SHIPMENT);
        order.setConfirmPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }
    
    public OrderCountVO getOrderCount(Long userId) {
        OrderCountVO vo = new OrderCountVO();
        
        vo.setPendingPayment(countByStatus(userId, Constants.ORDER_STATUS_PENDING_PAYMENT));
        vo.setPendingShipment(countByStatus(userId, Constants.ORDER_STATUS_PENDING_SHIPMENT));
        vo.setPendingReceipt(countByStatus(userId, Constants.ORDER_STATUS_PENDING_RECEIPT));
        vo.setCompleted(countByStatus(userId, Constants.ORDER_STATUS_COMPLETED));
        vo.setCancelled(countByStatus(userId, Constants.ORDER_STATUS_CANCELLED));
        
        return vo;
    }
    
    private Integer countByStatus(Long userId, Integer status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
                .eq(Order::getStatus, status);
        return Math.toIntExact(orderMapper.selectCount(wrapper));
    }
    
    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        
        List<OrderItemVO> itemVOs = items.stream()
                .map(this::convertItemToVO)
                .collect(Collectors.toList());
        vo.setItems(itemVOs);
        vo.setItemCount(items.size());
        
        if (!items.isEmpty()) {
            vo.setFirstProductImage(items.get(0).getProductImage());
        }
        
        return vo;
    }
    
    private OrderDetailVO convertToDetailVO(Order order) {
        OrderDetailVO vo = new OrderDetailVO();
        BeanUtils.copyProperties(order, vo);
        
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        
        List<OrderItemVO> itemVOs = items.stream()
                .map(this::convertItemToVO)
                .collect(Collectors.toList());
        vo.setItems(itemVOs);
        
        return vo;
    }
    
    private OrderItemVO convertItemToVO(OrderItem item) {
        OrderItemVO vo = new OrderItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }
    
    private Product convertToProduct(Object obj) {
        if (obj instanceof Product) {
            return (Product) obj;
        }
        return null;
    }
}
