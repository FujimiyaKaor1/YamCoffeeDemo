package com.yamcoffee.controller;

import com.yamcoffee.common.Constants;
import com.yamcoffee.common.Result;
import com.yamcoffee.dto.CartDTO;
import com.yamcoffee.dto.CartUpdateDTO;
import com.yamcoffee.service.CartService;
import com.yamcoffee.vo.CartVO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    
    private final CartService cartService;
    
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping("/list")
    public Result<List<CartVO>> getCartList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        List<CartVO> carts = cartService.getCartList(userId);
        return Result.success(carts);
    }
    
    @PostMapping
    public Result<CartVO> addToCart(HttpServletRequest request, @Valid @RequestBody CartDTO dto) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        CartVO cart = cartService.addToCart(userId, dto);
        return Result.success(cart);
    }
    
    @PutMapping("/{id}")
    public Result<CartVO> updateCart(@PathVariable Long id, HttpServletRequest request,
                                     @RequestBody CartUpdateDTO dto) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        CartVO cart = cartService.updateCart(id, userId, dto);
        return Result.success(cart);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteCart(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        cartService.deleteCart(id, userId);
        return Result.success();
    }
    
    @PutMapping("/{id}/select")
    public Result<Void> selectCart(@PathVariable Long id, HttpServletRequest request,
                                   @RequestParam Integer selected) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        cartService.selectCart(id, userId, selected);
        return Result.success();
    }
    
    @PutMapping("/selectAll")
    public Result<Void> selectAllCart(HttpServletRequest request, @RequestParam Integer selected) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        cartService.selectAllCart(userId, selected);
        return Result.success();
    }
}
