package com.yamcoffee.controller;

import com.yamcoffee.common.PageResult;
import com.yamcoffee.common.Result;
import com.yamcoffee.service.ProductService;
import com.yamcoffee.vo.ProductVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/list")
    public Result<PageResult<ProductVO>> getProductList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer isHot,
            @RequestParam(required = false) Integer isNew,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<ProductVO> result = productService.getProductList(categoryId, keyword, isHot, isNew, pageNum, pageSize);
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<ProductVO> getProductById(@PathVariable Long id) {
        ProductVO product = productService.getProductById(id);
        return Result.success(product);
    }
    
    @GetMapping("/recommend")
    public Result<List<ProductVO>> getRecommendProducts() {
        List<ProductVO> products = productService.getRecommendProducts();
        return Result.success(products);
    }
    
    @GetMapping("/hot")
    public Result<List<ProductVO>> getHotProducts() {
        List<ProductVO> products = productService.getHotProducts();
        return Result.success(products);
    }
}
