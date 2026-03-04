-- YamCoffee Database Initialization Script
SET NAMES utf8mb4;
USE yamcoffee;

-- User table
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    openid VARCHAR(64) NOT NULL UNIQUE COMMENT '微信openid',
    union_id VARCHAR(64) COMMENT '微信unionid',
    nickname VARCHAR(64) COMMENT '用户昵称',
    avatar_url VARCHAR(512) COMMENT '头像URL',
    phone VARCHAR(20) COMMENT '手机号',
    gender TINYINT DEFAULT 0 COMMENT '性别 0未知 1男 2女',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    INDEX idx_openid (openid),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- Category table
DROP TABLE IF EXISTS t_category;
CREATE TABLE t_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(32) NOT NULL COMMENT '分类名称',
    icon VARCHAR(512) COMMENT '分类图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- Product table
DROP TABLE IF EXISTS t_product;
CREATE TABLE t_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(128) NOT NULL COMMENT '商品名称',
    subtitle VARCHAR(256) COMMENT '商品副标题',
    main_image VARCHAR(512) COMMENT '主图URL',
    sub_images TEXT COMMENT '副图URLs JSON数组',
    detail TEXT COMMENT '商品详情 富文本',
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    original_price DECIMAL(10,2) COMMENT '原价',
    stock INT DEFAULT 0 COMMENT '库存',
    sales INT DEFAULT 0 COMMENT '销量',
    unit VARCHAR(16) COMMENT '单位',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热销',
    is_new TINYINT DEFAULT 0 COMMENT '是否新品',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_hot (is_hot),
    INDEX idx_new (is_new),
    FOREIGN KEY (category_id) REFERENCES t_category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- Address table
DROP TABLE IF EXISTS t_address;
CREATE TABLE t_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(32) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(32) NOT NULL COMMENT '省',
    city VARCHAR(32) NOT NULL COMMENT '市',
    district VARCHAR(32) NOT NULL COMMENT '区',
    detail_address VARCHAR(256) NOT NULL COMMENT '详细地址',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认 0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES t_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- Cart table
DROP TABLE IF EXISTS t_cart;
CREATE TABLE t_cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    selected TINYINT DEFAULT 1 COMMENT '是否选中 0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES t_user(id),
    FOREIGN KEY (product_id) REFERENCES t_product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- Order table
DROP TABLE IF EXISTS t_order;
CREATE TABLE t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    
    receiver_name VARCHAR(32) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_province VARCHAR(32) NOT NULL COMMENT '省',
    receiver_city VARCHAR(32) NOT NULL COMMENT '市',
    receiver_district VARCHAR(32) NOT NULL COMMENT '区',
    receiver_address VARCHAR(256) NOT NULL COMMENT '详细地址',
    
    total_amount DECIMAL(10,2) NOT NULL COMMENT '商品总金额',
    freight_amount DECIMAL(10,2) DEFAULT 0 COMMENT '运费',
    discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    
    status TINYINT DEFAULT 0 COMMENT '订单状态 0待付款 1待发货 2待收货 3已完成 4已取消',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态 0未支付 1已支付 2已退款',
    pay_type TINYINT COMMENT '支付方式 1微信支付',
    pay_time DATETIME COMMENT '支付时间',
    
    delivery_type TINYINT DEFAULT 1 COMMENT '配送方式 1快递',
    delivery_company VARCHAR(32) COMMENT '快递公司',
    delivery_no VARCHAR(64) COMMENT '快递单号',
    delivery_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    estimated_arrival_time DATETIME COMMENT '预计送达时间',
    
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(256) COMMENT '取消原因',
    
    user_note VARCHAR(256) COMMENT '用户备注',
    
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (user_id) REFERENCES t_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- Order item table
DROP TABLE IF EXISTS t_order_item;
CREATE TABLE t_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单项ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(128) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(512) COMMENT '商品图片',
    product_price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '购买数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    FOREIGN KEY (order_id) REFERENCES t_order(id),
    FOREIGN KEY (product_id) REFERENCES t_product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- Favorite table
DROP TABLE IF EXISTS t_favorite;
CREATE TABLE t_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES t_user(id),
    FOREIGN KEY (product_id) REFERENCES t_product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- Insert test data for categories
INSERT INTO t_category (name, icon, sort_order, status) VALUES
('麻山药咖啡', '/images/category/coffee.png', 1, 1),
('麻山药饮品', '/images/category/drink.png', 2, 1),
('麻山药零食', '/images/category/snack.png', 3, 1),
('麻山药礼盒', '/images/category/gift.png', 4, 1),
('周边文创', '/images/category/merch.png', 5, 1);

-- Insert test data for products
INSERT INTO t_product (category_id, name, subtitle, main_image, price, original_price, stock, sales, unit, status, is_hot, is_new, is_recommend) VALUES
(1, '麻山药拿铁', '香浓咖啡与麻山药的完美融合', '/images/product/yam-latte.png', 28.00, 32.00, 100, 256, '杯', 1, 1, 0, 1),
(1, '麻山药美式', '清爽美式遇上养生麻山药', '/images/product/yam-americano.png', 22.00, 26.00, 100, 189, '杯', 1, 0, 1, 1),
(1, '麻山药摩卡', '巧克力与麻山药的甜蜜邂逅', '/images/product/yam-mocha.png', 32.00, 36.00, 80, 145, '杯', 1, 1, 0, 1),
(1, '麻山药卡布奇诺', '绵密奶泡与麻山药的浪漫', '/images/product/yam-cappuccino.png', 30.00, 34.00, 90, 178, '杯', 1, 0, 0, 1),
(2, '麻山药奶茶', '丝滑奶茶养生新选择', '/images/product/yam-milktea.png', 18.00, 22.00, 150, 423, '杯', 1, 1, 0, 1),
(2, '麻山药燕麦奶', '健康燕麦与麻山药', '/images/product/yam-oatmilk.png', 20.00, 24.00, 120, 234, '杯', 1, 0, 1, 1),
(2, '麻山药鲜榨汁', '新鲜麻山药原汁原味', '/images/product/yam-juice.png', 16.00, 20.00, 80, 167, '杯', 1, 0, 0, 0),
(3, '麻山药脆片', '酥脆可口健康零食', '/images/product/yam-chips.png', 12.00, 15.00, 200, 567, '袋', 1, 1, 0, 1),
(3, '麻山药饼干', '营养美味的休闲小食', '/images/product/yam-cookie.png', 15.00, 18.00, 180, 345, '盒', 1, 0, 1, 1),
(3, '麻山药蛋糕', '松软香甜的下午茶', '/images/product/yam-cake.png', 38.00, 45.00, 50, 123, '份', 1, 0, 0, 1),
(4, '麻山药咖啡礼盒', '精选组合送礼首选', '/images/product/yam-giftbox1.png', 168.00, 198.00, 30, 89, '盒', 1, 1, 0, 1),
(4, '麻山药零食礼盒', '健康美味全家共享', '/images/product/yam-giftbox2.png', 128.00, 158.00, 40, 67, '盒', 1, 0, 0, 1),
(5, '麻山药主题马克杯', '精致设计品质生活', '/images/product/yam-mug.png', 48.00, 58.00, 100, 234, '个', 1, 0, 0, 1),
(5, '麻山药帆布袋', '时尚环保出行必备', '/images/product/yam-bag.png', 35.00, 45.00, 150, 189, '个', 1, 0, 1, 0);
