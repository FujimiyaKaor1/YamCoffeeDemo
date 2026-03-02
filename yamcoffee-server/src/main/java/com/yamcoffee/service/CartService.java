package com.yamcoffee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yamcoffee.common.ResultCode;
import com.yamcoffee.dto.CartDTO;
import com.yamcoffee.dto.CartUpdateDTO;
import com.yamcoffee.entity.Cart;
import com.yamcoffee.entity.Product;
import com.yamcoffee.exception.BusinessException;
import com.yamcoffee.mapper.CartMapper;
import com.yamcoffee.mapper.ProductMapper;
import com.yamcoffee.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    
    @Autowired
    public CartService(CartMapper cartMapper, ProductMapper productMapper) {
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }
    
    public List<CartVO> getCartList(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getCreateTime);
        
        List<Cart> carts = cartMapper.selectList(wrapper);
        return carts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CartVO addToCart(Long userId, CartDTO dto) {
        Product product = productMapper.selectById(dto.getProductId());
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXIST);
        }
        
        if (product.getStock() < dto.getQuantity()) {
            throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH);
        }
        
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, dto.getProductId());
        Cart existCart = cartMapper.selectOne(wrapper);
        
        if (existCart != null) {
            int newQuantity = existCart.getQuantity() + dto.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH);
            }
            existCart.setQuantity(newQuantity);
            cartMapper.updateById(existCart);
            return convertToVO(existCart);
        }
        
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(dto.getProductId());
        cart.setQuantity(dto.getQuantity());
        cart.setSelected(1);
        cartMapper.insert(cart);
        
        return convertToVO(cart);
    }
    
    @Transactional
    public CartVO updateCart(Long id, Long userId, CartUpdateDTO dto) {
        Cart cart = getCartByIdAndUserId(id, userId);
        
        if (dto.getQuantity() != null) {
            Product product = productMapper.selectById(cart.getProductId());
            if (product.getStock() < dto.getQuantity()) {
                throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH);
            }
            cart.setQuantity(dto.getQuantity());
        }
        
        if (dto.getSelected() != null) {
            cart.setSelected(dto.getSelected());
        }
        
        cartMapper.updateById(cart);
        return convertToVO(cart);
    }
    
    @Transactional
    public void deleteCart(Long id, Long userId) {
        Cart cart = getCartByIdAndUserId(id, userId);
        cartMapper.deleteById(cart.getId());
    }
    
    @Transactional
    public void selectCart(Long id, Long userId, Integer selected) {
        Cart cart = getCartByIdAndUserId(id, userId);
        cart.setSelected(selected);
        cartMapper.updateById(cart);
    }
    
    @Transactional
    public void selectAllCart(Long userId, Integer selected) {
        LambdaUpdateWrapper<Cart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Cart::getUserId, userId)
                .set(Cart::getSelected, selected);
        cartMapper.update(null, updateWrapper);
    }
    
    @Transactional
    public void deleteSelectedCartItems(Long userId, List<Long> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return;
        }
        
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .in(Cart::getId, cartItemIds);
        cartMapper.delete(wrapper);
    }
    
    public List<Cart> getSelectedCartItems(Long userId, List<Long> cartItemIds) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .in(Cart::getId, cartItemIds)
                .eq(Cart::getSelected, 1);
        return cartMapper.selectList(wrapper);
    }
    
    private Cart getCartByIdAndUserId(Long id, Long userId) {
        Cart cart = cartMapper.selectById(id);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.CART_ITEM_NOT_EXIST);
        }
        return cart;
    }
    
    private CartVO convertToVO(Cart cart) {
        CartVO vo = new CartVO();
        vo.setId(cart.getId());
        vo.setProductId(cart.getProductId());
        vo.setQuantity(cart.getQuantity());
        vo.setSelected(cart.getSelected());
        
        Product product = productMapper.selectById(cart.getProductId());
        if (product != null) {
            vo.setProductName(product.getName());
            vo.setProductImage(product.getMainImage());
            vo.setProductPrice(product.getPrice());
            vo.setStock(product.getStock());
            vo.setSubtotal(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
        }
        
        return vo;
    }
}
