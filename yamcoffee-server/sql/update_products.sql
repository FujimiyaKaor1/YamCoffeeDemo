USE yamcoffee;

-- 先清空现有商品
DELETE FROM t_product;

-- 重新插入实际商品
INSERT INTO t_product (id, category_id, name, subtitle, main_image, description, detail, price, original_price, stock, sales, unit, status, is_hot, is_new, is_recommend, create_time, update_time) VALUES
(1, 1, '麻山药咖啡', '健康养生新选择', '/images/product/yam-coffee.png', '精选麻山药与优质咖啡豆完美融合', '麻山药咖啡，采用本地优质麻山药粉与进口咖啡豆精心调配，口感醇厚，营养丰富。', 28.00, 32.00, 100, 0, '杯', 1, 1, 1, 1, NOW(), NOW()),
(2, 2, '冰糖山药罐头', '清甜可口即食佳品', '/images/product/yam-canned-sugar.png', '精选麻山药冰糖熬制', '冰糖山药罐头，选用新鲜麻山药，配以优质冰糖传统工艺熬制，开罐即食，方便美味。', 18.00, 22.00, 200, 0, '罐', 1, 1, 0, 1, NOW(), NOW()),
(3, 3, '麻山药醋', '传统酿造健康调味', '/images/product/yam-vinegar.png', '麻山药发酵酿造食醋', '麻山药醋，采用传统固态发酵工艺，以麻山药为主要原料酿造而成，酸香浓郁，风味独特。', 25.00, 30.00, 150, 0, '瓶', 1, 0, 1, 1, NOW(), NOW()),
(4, 2, '麻山药罐头', '原汁原味营养保留', '/images/product/yam-canned.png', '新鲜麻山药即食罐头', '麻山药罐头，保留麻山药原有营养和口感，无添加防腐剂，健康美味，老少皆宜。', 15.00, 18.00, 180, 0, '罐', 1, 1, 1, 0, NOW(), NOW()),
(5, 4, '麻山药酒', '养生佳酿醇香回味', '/images/product/yam-wine.png', '麻山药浸泡养生酒', '麻山药酒，精选优质白酒浸泡新鲜麻山药，酒香与山药香完美融合，适量饮用有益健康。', 88.00, 108.00, 50, 0, '瓶', 1, 0, 0, 1, NOW(), NOW()),
(6, 5, '蠡县特产礼包', '地方特色送礼首选', '/images/product/gift-box.png', '蠡县麻山药特产组合装', '蠡县特产礼包，包含麻山药咖啡、麻山药罐头、麻山药醋等特色产品，包装精美，送礼佳选。', 168.00, 198.00, 30, 0, '盒', 1, 1, 1, 1, NOW(), NOW());

-- 更新分类
DELETE FROM t_category;
INSERT INTO t_category (id, name, icon, sort, status, create_time, update_time) VALUES
(1, '咖啡饮品', '/images/category/coffee.png', 1, 1, NOW(), NOW()),
(2, '罐头食品', '/images/category/canned.png', 2, 1, NOW(), NOW()),
(3, '调味品', '/images/category/condiment.png', 3, 1, NOW(), NOW()),
(4, '酒类', '/images/category/wine.png', 4, 1, NOW(), NOW()),
(5, '礼盒套装', '/images/category/gift.png', 5, 1, NOW(), NOW());
