-- 添加支付确认相关字段到订单表
-- 执行前请备份数据

ALTER TABLE t_order 
ADD COLUMN user_pay_time DATETIME DEFAULT NULL COMMENT '用户支付确认时间',
ADD COLUMN confirm_pay_time DATETIME DEFAULT NULL COMMENT '商家确认收款时间';

-- 查看修改结果
DESC t_order;

-- 支付配置表
CREATE TABLE IF NOT EXISTS t_payment_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    qr_code_url VARCHAR(500) DEFAULT NULL COMMENT '二维码图片URL',
    wechat_qr_code_url VARCHAR(500) DEFAULT NULL COMMENT '微信收款码URL',
    alipay_qr_code_url VARCHAR(500) DEFAULT NULL COMMENT '支付宝收款码URL',
    description VARCHAR(200) DEFAULT NULL COMMENT '收款说明',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付配置表';

-- 插入默认配置
INSERT INTO t_payment_config (qr_code_url, wechat_qr_code_url, alipay_qr_code_url, description, status)
VALUES ('', '', '', '扫码支付', 1);
