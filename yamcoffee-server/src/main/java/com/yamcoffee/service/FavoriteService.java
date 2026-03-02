package com.yamcoffee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yamcoffee.entity.Favorite;
import com.yamcoffee.entity.Product;
import com.yamcoffee.exception.BusinessException;
import com.yamcoffee.mapper.FavoriteMapper;
import com.yamcoffee.mapper.ProductMapper;
import com.yamcoffee.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    
    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;
    
    public FavoriteService(FavoriteMapper favoriteMapper, ProductMapper productMapper) {
        this.favoriteMapper = favoriteMapper;
        this.productMapper = productMapper;
    }
    
    public List<ProductVO> getFavoriteList(Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        
        List<Favorite> favorites = favoriteMapper.selectList(wrapper);
        
        return favorites.stream()
                .map(favorite -> {
                    Product product = productMapper.selectById(favorite.getProductId());
                    if (product != null && product.getStatus() == 1) {
                        ProductVO vo = new ProductVO();
                        BeanUtils.copyProperties(product, vo);
                        return vo;
                    }
                    return null;
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void addFavorite(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException(com.yamcoffee.common.ResultCode.PRODUCT_NOT_EXIST);
        }
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId);
        Favorite existFavorite = favoriteMapper.selectOne(wrapper);
        
        if (existFavorite == null) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setProductId(productId);
            favoriteMapper.insert(favorite);
        }
    }
    
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId);
        favoriteMapper.delete(wrapper);
    }
    
    public boolean isFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId);
        return favoriteMapper.selectCount(wrapper) > 0;
    }
}
