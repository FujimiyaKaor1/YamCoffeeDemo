package com.yamcoffee.controller;

import com.yamcoffee.common.Constants;
import com.yamcoffee.common.Result;
import com.yamcoffee.service.FavoriteService;
import com.yamcoffee.vo.ProductVO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }
    
    @GetMapping("/list")
    public Result<List<ProductVO>> getFavoriteList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        List<ProductVO> favorites = favoriteService.getFavoriteList(userId);
        return Result.success(favorites);
    }
    
    @PostMapping
    public Result<Void> addFavorite(HttpServletRequest request, @RequestBody Map<String, Long> body) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        Long productId = body.get("productId");
        favoriteService.addFavorite(userId, productId);
        return Result.success();
    }
    
    @DeleteMapping("/{productId}")
    public Result<Void> removeFavorite(@PathVariable Long productId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        favoriteService.removeFavorite(userId, productId);
        return Result.success();
    }
    
    @GetMapping("/check/{productId}")
    public Result<Map<String, Boolean>> checkFavorite(@PathVariable Long productId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.USER_ID_KEY);
        boolean isFavorite = favoriteService.isFavorite(userId, productId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isFavorite", isFavorite);
        return Result.success(result);
    }
}
