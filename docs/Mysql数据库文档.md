# Mysql建表语句

## （1）用户表
### 创建用户表
```
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `email` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '邮箱',
  `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) COLLATE utf8_bin NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin NULL COMMENT '性别',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='用户信息';
```
### 插入用户数据
```
INSERT INTO `user` (`email`, `username`, `password`, `phone`, `sex`, `create_time`, `update_time`) 
VALUES ('example@example.com', 'exampleUser', 'e10adc3949ba59abbe56e057f20f883e', '13800138000', '男', NOW(), NOW());
```

## （2）分类表
### 创建分类表
```
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '商品分类名称',
    `sort` int NOT NULL DEFAULT '0' COMMENT '顺序',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_category_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='商品分类表';
```

### 插入分类数据
```
INSERT INTO `category` (`name`, `sort`, `create_time`, `update_time`)
VALUES
    ('phone', 1, NOW(), NOW()),
    ('clothes', 2, NOW(), NOW()),
    ('computer', 3, NOW(), NOW());
```

## （3）商品表
### 创建商品表
```
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '商品名称',
                           `category_id` bigint NOT NULL COMMENT '商品对应的分类id',
                           `price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
                           `picture` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
                           `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `idx_product_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='商品表';
```

### 插入商品数据
```
INSERT INTO `product` (`name`, `category_id`, `price`, `picture`, `description`, `create_time`, `update_time`)
VALUES
    ('华为手机', 1, 699.99, 'https://tse4-mm.cn.bing.net/th/id/OIP-C.T56b3nhP9I43rcrMOTAl1AHaHa?w=169&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7', '中国智能手机', NOW(), NOW()),
    ('小米手机', 1, 799.99, 'https://tse1-mm.cn.bing.net/th/id/OIP-C.vk_eXN4CdNb6vWEUlDuszgHaHa?w=177&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7', '中国高性能手机', NOW(), NOW()),
    ('苹果手机', 1, 899.99, 'https://tse2-mm.cn.bing.net/th/id/OIP-C.WIrbwBdgo1OcoSbtERQNPwHaHa?w=185&h=185&c=7&r=0&o=5&dpr=1.5&pid=1.7', '美国手机', NOW(), NOW()),
    ('中兴手机', 1, 999.99, 'https://tse3-mm.cn.bing.net/th/id/OIP-C.58z_UWvWcvls-RsM7P5D7wAAAA?w=138&h=150&c=7&r=0&o=5&dpr=1.5&pid=1.7', '中国高性能智能手机', NOW(), NOW());

-- 插入分类为“衣服”的商品
INSERT INTO `product` (`name`, `category_id`, `price`, `picture`, `description`, `create_time`, `update_time`)
VALUES
    ('T恤', 2, 29.99, 'https://tse3-mm.cn.bing.net/th/id/OIP-C.2IjFo05mspA6vsGGvBomHAHaHa?w=214&h=215&c=7&r=0&o=5&dpr=1.5&pid=1.7', '舒适纯棉T恤A', NOW(), NOW()),
    ('短裤', 2, 39.99, 'https://tse2-mm.cn.bing.net/th/id/OIP-C.-V3mJJdtYFFzKPHud3v3dQHaHa?w=213&h=213&c=7&r=0&o=5&dpr=1.5&pid=1.7', '舒适纯棉T恤B', NOW(), NOW()),
    ('毛衣', 2, 49.99, 'https://tse2-mm.cn.bing.net/th/id/OIP-C.iAVcS160cvBKVthAoTFNAAHaHa?w=189&h=189&c=7&r=0&o=5&dpr=1.5&pid=1.7', '舒适纯棉T恤C', NOW(), NOW()),
    ('羽绒服', 2, 59.99, 'https://tse1-mm.cn.bing.net/th/id/OIP-C.dMG5DDVshpKXyxsb7jMyUwHaHa?w=202&h=202&c=7&r=0&o=5&dpr=1.5&pid=1.7', '舒适纯棉T恤D', NOW(), NOW());

-- 插入分类为“电脑”的商品
INSERT INTO `product` (`name`, `category_id`, `price`, `picture`, `description`, `create_time`, `update_time`)
VALUES
    ('DELL', 3, 4999.99, 'https://tse1-mm.cn.bing.net/th/id/OIP-C.2GWGKhcwGTZP9G_Vu_x3dwHaHa?w=193&h=193&c=7&r=0&o=5&dpr=1.5&pid=1.7', '戴尔高性能笔记本电脑A', NOW(), NOW()),
    ('MacBook', 3, 5999.99, 'https://tse3-mm.cn.bing.net/th/id/OIP-C.taDD9tJ-R0DSzkqyspXHEwHaHa?w=171&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7', '苹果高性能笔记本电脑B', NOW(), NOW()),
    ('Lenovo', 3, 6999.99, 'https://tse4-mm.cn.bing.net/th/id/OIP-C.x_wTh4zWByF5_bCW4oqXGQHaFj?w=251&h=188&c=7&r=0&o=5&dpr=1.5&pid=1.7', '联想高性能笔记本电脑C', NOW(), NOW()),
    ('Huawei', 3, 7999.99, 'https://tse2-mm.cn.bing.net/th/id/OIP-C.dhNrLlQp1U7t7siqaC4OyQHaE8?w=259&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7', '华为高性能笔记本电脑D', NOW(), NOW());
```

## （4）购物车表
### 创建购物车表
```
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  `picture` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `product_id` bigint NOT NULL COMMENT '商品id',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
`status` tinyint DEFAULT 1 COMMENT '1: 有效, 0: 无效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
INDEX idx_user_id (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='购物车';
```


## （5）地址簿表
### 创建地址簿表
```
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `username` varchar(50) COLLATE utf8_bin  NOT NULL COMMENT '用户名/收货人',
  `email` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '邮箱',
  `detail` varchar(200) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '详细地址',
  `city` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '市名称',
  `province` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '省名称',
  `country` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '国家名称',
  `label` varchar(10) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '标签（家/公司/学校）',
  `zip_code` varchar(10) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '邮政编码',
  PRIMARY KEY (`id`),
  INDEX idx_user_id (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='用户地址簿';
```

### 插入地址簿表（请根据实际情况修改user_id, username, email !）
```
INSERT INTO `address_book` (`user_id`, `username`, `email`, `detail`, `city`, `province`, `country`, `label`, `zip_code`)
VALUES 
(1, 'xx', 'xx@qq.com', '朝阳区建国门外大街22号赛特大厦10层1002室', '北京市', '北京市', '中国', '公司', '100004'),
(1, 'xx', 'xx@qq.com', '浦东新区世纪大道100号上海环球金融中心20楼2005室', '上海市', '上海市', '中国', '家', '200120'),
(1, 'xx', 'xx@qq.com', '天河区五山路381号广东大学大学城校区12栋305宿舍', '广州市', '广东省', '中国', '学校', '510640');
```

## （6）订单表
### 创建订单表
```
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '下单用户',
  `address_book_id` bigint NOT NULL COMMENT '地址id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `pay_method` int NOT NULL DEFAULT '1' COMMENT '支付方式 1微信,2支付宝',
  `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `is_paid` int NOT NULL DEFAULT '0' COMMENT '0未支付1已支付',
  `username` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名称',
  `email` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `address_detail` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '完整详细地址',
   PRIMARY KEY (`id`),
   INDEX idx_user_id (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='订单表';
```

## （7）订单明细表
### 创建订单明细表
```
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  `picture` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `product_id` bigint NOT NULL COMMENT '商品id',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '商品数量',
  `amount` decimal(10,2) NOT NULL COMMENT '商品单价',
  PRIMARY KEY (`id`),
  INDEX idx_order_id (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='订单明细表';
```

## （8）支付表
### 创建支付表
```
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `transaction_number` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '交易号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `order_number` varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '订单号',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `pay_method` int NOT NULL DEFAULT '1' COMMENT '支付方式 1微信,2支付宝',
  `order_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `is_paid` int NOT NULL DEFAULT '0' COMMENT '0未支付1已支付',
   PRIMARY KEY (`id`),
   INDEX idx_user_id (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='支付表';
```