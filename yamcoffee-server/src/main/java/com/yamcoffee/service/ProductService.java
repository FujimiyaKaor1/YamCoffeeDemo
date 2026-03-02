package com.yamcoffee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yamcoffee.common.PageResult;
import com.yamcoffee.common.ResultCode;
import com.yamcoffee.entity.Product;
import com.yamcoffee.exception.BusinessException;
import com.yamcoffee.mapper.ProductMapper;
import com.yamcoffee.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final ProductMapper productMapper;
    
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }
    
    public PageResult<ProductVO> getProductList(Long categoryId, String keyword, Integer isHot, Integer isNew, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Product::getName, keyword);
        }
        if (isHot != null && isHot == 1) {
            wrapper.eq(Product::getIsHot, 1);
        }
        if (isNew != null && isNew == 1) {
            wrapper.eq(Product::getIsNew, 1);
        }
        
        wrapper.orderByDesc(Product::getSortOrder);
        
        Page<Product> page = new Page<>(pageNum, pageSize);
        Page<Product> result = productMapper.selectPage(page, wrapper);
        
        List<ProductVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageResult<>(result.getTotal(), pageNum, pageSize, voList);
    }
    
    public ProductVO getProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXIST);
        }
        return convertToVO(product);
    }
    
    public List<ProductVO> getRecommendProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .eq(Product::getIsRecommend, 1)
                .orderByDesc(Product::getSales)
                .last("LIMIT 10");
        
        List<Product> products = productMapper.selectList(wrapper);
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    public List<ProductVO> getHotProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .eq(Product::getIsHot, 1)
                .orderByDesc(Product::getSales)
                .last("LIMIT 10");
        
        List<Product> products = productMapper.selectList(wrapper);
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return vo;
    }
}
