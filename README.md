# YamCoffee 麻山药特产商城小程序

一款基于微信小程序的麻山药特产电商平台，支持商品浏览、在线下单、扫码支付等完整购物流程。

## 项目简介

YamCoffee 是一个面向蠡县麻山药特产销售的电商小程序，采用前后端分离架构，后端使用 Spring Boot + MyBatis Plus，前端使用微信小程序原生开发。项目实现了完整的电商核心功能，包括商品展示、订单管理、支付流程等。

## 功能特性

### 用户端功能
- 微信一键登录
- 商品分类浏览
- 商品搜索
- 商品详情查看
- 商品收藏
- 收货地址管理
- 直接下单购买
- 订单管理（查看、取消、确认收货）
- 扫码支付流程

### 商家端功能
- 订单管理
- 确认收款
- 安排配送

## 技术栈

### 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.x | 基础框架 |
| MyBatis Plus | 3.5.x | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| Maven | 3.6+ | 项目构建 |

### 前端技术
| 技术 | 说明 |
|------|------|
| 微信小程序原生 | 前端框架 |
| WXSS | 样式语言 |

## 项目结构

```
YamCoffee_demo/
├── yamcoffee-server/                    # 后端项目
│   ├── src/main/java/com/yamcoffee/
│   │   ├── controller/                  # 控制器层
│   │   ├── service/                     # 服务层
│   │   ├── mapper/                      # 数据访问层
│   │   ├── entity/                      # 实体类
│   │   ├── dto/                         # 数据传输对象
│   │   ├── vo/                          # 视图对象
│   │   ├── common/                      # 公共类
│   │   ├── exception/                   # 异常处理
│   │   └── utils/                       # 工具类
│   ├── src/main/resources/
│   │   └── application.yml              # 配置文件
│   └── sql/                             # SQL脚本
│
├── yamcoffee-miniprogram/               # 小程序项目
│   ├── pages/                           # 主包页面
│   │   ├── index/                       # 首页
│   │   ├── order/                       # 订单页
│   │   └── mine/                        # 我的页
│   ├── subpages/                        # 分包页面
│   │   ├── product/                     # 商品相关
│   │   ├── order/                       # 订单相关
│   │   ├── address/                     # 地址相关
│   │   ├── favorite/                    # 收藏相关
│   │   └── login/                       # 登录页
│   ├── components/                      # 公共组件
│   │   └── payment-modal/               # 支付弹窗组件
│   ├── images/                          # 图片资源
│   ├── utils/                           # 工具类
│   ├── app.js                           # 小程序入口
│   └── app.json                         # 小程序配置
│
└── README.md                            # 项目说明
```

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- 微信开发者工具

### 后端部署

1. **克隆项目**
```bash
git clone https://github.com/FujimiyaKaor1/YamCoffeeDemo.git
cd YamCoffeeDemo
```

2. **创建数据库**
```sql
CREATE DATABASE yamcoffee DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **执行SQL脚本**
```bash
# 按顺序执行以下脚本
mysql -u root -p yamcoffee < yamcoffee-server/sql/init.sql
mysql -u root -p yamcoffee < yamcoffee-server/sql/update_payment_fields.sql
mysql -u root -p yamcoffee < yamcoffee-server/sql/update_products.sql
```

4. **修改配置文件**

编辑 `yamcoffee-server/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yamcoffee?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: 你的数据库用户名
    password: 你的数据库密码

wx:
  appid: 你的微信小程序AppID
  secret: 你的微信小程序Secret
```

5. **启动后端服务**
```bash
cd yamcoffee-server
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

### 小程序配置

1. **打开微信开发者工具**，导入 `yamcoffee-miniprogram` 目录

2. **修改AppID**

编辑 `project.config.json`：
```json
{
  "appid": "你的微信小程序AppID"
}
```

3. **修改后端地址**

编辑 `app.js`：
```javascript
globalData: {
  baseUrl: 'http://你的服务器地址:8080/api/v1'
}
```

4. **本地调试设置**

在微信开发者工具中：
- 点击右上角「详情」→「本地设置」
- 勾选「不校验合法域名」

5. **点击编译运行**

## 商品信息

当前已配置的商品：

| 商品名称 | 价格 | 分类 |
|---------|------|------|
| 麻山药咖啡 | ¥28.00 | 咖啡饮品 |
| 冰糖山药罐头 | ¥18.00 | 罐头食品 |
| 麻山药醋 | ¥25.00 | 调味品 |
| 麻山药罐头 | ¥15.00 | 罐头食品 |
| 麻山药酒 | ¥88.00 | 酒类 |
| 蠡县特产礼包 | ¥168.00 | 礼盒套装 |

## 支付流程

本项目采用扫码支付方式：

```
用户下单 → 弹出收款二维码 → 用户扫码付款 → 点击"我已支付" 
→ 商家确认收款 → 安排配送
```

### 配置收款二维码

在数据库 `t_payment_config` 表中更新收款二维码图片URL：
```sql
UPDATE t_payment_config 
SET wechat_qr_code_url = '微信收款码图片URL',
    alipay_qr_code_url = '支付宝收款码图片URL'
WHERE id = 1;
```

## API接口文档

### 用户接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /user/login | POST | 微信登录 |
| /user/info | GET | 获取用户信息 |

### 商品接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /product/list | GET | 商品列表 |
| /product/{id} | GET | 商品详情 |
| /product/hot | GET | 热销商品 |
| /product/recommend | GET | 推荐商品 |

### 订单接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /order/direct | POST | 直接下单 |
| /order/list | GET | 订单列表 |
| /order/{orderNo} | GET | 订单详情 |
| /order/{orderNo}/cancel | PUT | 取消订单 |
| /order/{orderNo}/confirm-pay | PUT | 用户确认支付 |
| /order/{orderNo}/confirm-receipt-payment | PUT | 商家确认收款 |

### 支付接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /payment/qrcode | GET | 获取收款二维码 |

## 手机真机测试

1. 确保手机和电脑在同一WiFi网络
2. 查看电脑IP地址：`ipconfig`
3. 修改 `app.js` 中的 `baseUrl` 为电脑IP
4. 配置Windows防火墙允许8080端口
5. 微信开发者工具点击「预览」生成二维码
6. 手机微信扫码测试

## 生产环境部署

### 后端部署
1. 打包项目：`mvn clean package`
2. 上传jar包到服务器
3. 运行：`java -jar yamcoffee-server-1.0.0.jar`

### 域名配置
1. 在微信公众平台配置服务器域名
2. 使用HTTPS协议
3. 更新小程序中的 `baseUrl`

## 常见问题

### Q: 小程序提示网络错误？
A: 
- 检查后端服务是否启动
- 确认 `baseUrl` 配置正确
- 真机测试需配置防火墙

### Q: 登录失败？
A: 
- 检查微信AppID和Secret配置
- 确认小程序已关联正确的AppID

### Q: 图片不显示？
A: 
- 当前使用占位图，需替换真实图片资源
- 将图片放入 `images` 目录对应文件夹

## 许可证

MIT License

## 联系方式

如有问题，请提交 Issue 或 Pull Request。
