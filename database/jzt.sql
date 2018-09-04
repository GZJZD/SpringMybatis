/*
Navicat MySQL Data Transfer

Source Server         : 47.52.77.50
Source Server Version : 50639
Source Host           : 47.52.77.50:3306
Source Database       : jzt

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-09-04 14:43:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for agent
-- ----------------------------
DROP TABLE IF EXISTS `agent`;
CREATE TABLE `agent` (
  `agent` varchar(255) DEFAULT NULL,
  `agentname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for client_net_position
-- ----------------------------
DROP TABLE IF EXISTS `client_net_position`;
CREATE TABLE `client_net_position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `follow_order_id` bigint(20) DEFAULT NULL,
  `net_position_sum` double DEFAULT NULL,
  `ticket` varchar(255) DEFAULT NULL,
  `open_time` varchar(255) DEFAULT NULL,
  `close_time` varchar(255) DEFAULT NULL,
  `create_time` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ticket` (`ticket`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for follow_order
-- ----------------------------
DROP TABLE IF EXISTS `follow_order`;
CREATE TABLE `follow_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `follow_order_name` varchar(255) DEFAULT NULL,
  `tb_variety_id` bigint(20) DEFAULT NULL,
  `tb_account_id` bigint(20) DEFAULT NULL,
  `follow_manner` tinyint(4) DEFAULT NULL,
  `max_profit` tinyint(4) DEFAULT NULL,
  `max_profit_number` double DEFAULT NULL,
  `max_loss` tinyint(4) DEFAULT NULL,
  `max_loss_number` double DEFAULT NULL,
  `account_loss` tinyint(4) DEFAULT NULL,
  `account_loss_number` double DEFAULT NULL,
  `order_point` tinyint(4) DEFAULT NULL,
  `client_point` tinyint(4) DEFAULT NULL,
  `client_point_number` double DEFAULT NULL,
  `net_position_direction` tinyint(4) DEFAULT NULL,
  `net_position_change` int(11) DEFAULT NULL,
  `net_position_follow_number` int(11) DEFAULT NULL,
  `net_position_sum` double DEFAULT NULL,
  `net_position_hold_number` int(11) DEFAULT NULL,
  `start_time` varchar(255) DEFAULT NULL,
  `follow_order_status` tinyint(4) DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `update_date` varchar(255) DEFAULT NULL,
  `update_by_user` bigint(20) DEFAULT NULL,
  `net_position_status` tinyint(4) DEFAULT NULL,
  `version` int(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for follow_order_client
-- ----------------------------
DROP TABLE IF EXISTS `follow_order_client`;
CREATE TABLE `follow_order_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `follow_order_id` bigint(20) DEFAULT NULL,
  `user_code` varchar(20) DEFAULT NULL,
  `platform_code` varchar(20) DEFAULT NULL,
  `follow_direction` tinyint(4) DEFAULT NULL,
  `hand_number_type` tinyint(4) DEFAULT NULL,
  `follow_hand_number` int(20) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `delete_date` varchar(255) DEFAULT NULL,
  `delete_by_user` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `update_by_user` bigint(20) DEFAULT NULL,
  `update_date` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for follow_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `follow_order_detail`;
CREATE TABLE `follow_order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(255) DEFAULT NULL,
  `trade_direction` tinyint(4) DEFAULT NULL,
  `hand_number` int(11) DEFAULT NULL,
  `original_hand_number` int(11) DEFAULT NULL,
  `remain_hand_number` int(11) DEFAULT NULL,
  `tb_account_id` bigint(20) DEFAULT NULL,
  `follow_order_client_id` bigint(20) DEFAULT NULL,
  `follow_order_trade_record_id` bigint(20) DEFAULT NULL,
  `open_price` double DEFAULT NULL,
  `open_time` varchar(255) DEFAULT NULL,
  `close_price` double DEFAULT NULL,
  `close_time` varchar(255) DEFAULT NULL,
  `poundage` double DEFAULT NULL,
  `profit_loss` double DEFAULT NULL,
  `client_profit` double DEFAULT NULL,
  `ticket` varchar(255) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `update_date` varchar(0) DEFAULT NULL,
  `update_by_user` bigint(20) DEFAULT NULL,
  `follow_order_id` bigint(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for follow_order_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `follow_order_trade_record`;
CREATE TABLE `follow_order_trade_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `variety_id` bigint(20) DEFAULT NULL,
  `hand_number` int(11) DEFAULT NULL,
  `trade_time` varchar(255) DEFAULT NULL,
  `trade_direction` tinyint(4) DEFAULT NULL,
  `open_close_type` tinyint(4) DEFAULT NULL,
  `market_price` double DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `follow_order_id` bigint(20) DEFAULT NULL,
  `follow_order_client_id` bigint(20) DEFAULT NULL,
  `ticket` varchar(255) DEFAULT NULL,
  `new_ticket` varchar(255) DEFAULT NULL,
  `poundage` double DEFAULT NULL,
  `client_net_position_id` bigint(20) DEFAULT NULL,
  `update_date` varchar(255) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `version` int(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for prices
-- ----------------------------
DROP TABLE IF EXISTS `prices`;
CREATE TABLE `prices` (
  `symbol` varchar(225) DEFAULT NULL,
  `bid` double DEFAULT NULL,
  `ask` double DEFAULT NULL,
  `high` double DEFAULT NULL,
  `low` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_account
-- ----------------------------
DROP TABLE IF EXISTS `tb_account`;
CREATE TABLE `tb_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tb_agent_id` bigint(20) NOT NULL,
  `tb_platform_id` bigint(20) NOT NULL,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `create_user` varchar(255) NOT NULL,
  `create_time` varchar(255) NOT NULL,
  `modify_time` varchar(255) DEFAULT NULL,
  `modify_user` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_agent
-- ----------------------------
DROP TABLE IF EXISTS `tb_agent`;
CREATE TABLE `tb_agent` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `order_by` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user` varchar(255) NOT NULL,
  `modify_time` datetime DEFAULT NULL,
  `modify_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_contract_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_contract_info`;
CREATE TABLE `tb_contract_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tb_variety_id` bigint(20) NOT NULL COMMENT '品种id',
  `tb_platform_id` bigint(20) DEFAULT NULL,
  `contract_code` varchar(255) NOT NULL COMMENT '合约代码',
  `contract_name` varchar(255) NOT NULL COMMENT '合约名称',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `modify_user` varchar(20) DEFAULT NULL,
  `create_user` varchar(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_contract_info_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_contract_info_link`;
CREATE TABLE `tb_contract_info_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract_info_id` bigint(20) DEFAULT NULL,
  `max_market_order_volumn` bigint(20) DEFAULT NULL,
  `min_market_order_volumn` bigint(20) DEFAULT NULL,
  `volumn_multiple` bigint(20) DEFAULT NULL,
  `price_tick` double DEFAULT NULL,
  `open_ratio_money` double DEFAULT NULL,
  `open_ratio_volumn` double DEFAULT NULL,
  `close_ratio_money` double DEFAULT NULL,
  `close_ratio_volumn` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_login
-- ----------------------------
DROP TABLE IF EXISTS `tb_login`;
CREATE TABLE `tb_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(15) DEFAULT NULL,
  `phone_number` varchar(19) DEFAULT NULL,
  `verify_time` varchar(19) DEFAULT NULL,
  `code` varchar(6) DEFAULT NULL,
  `create_date` varchar(225) DEFAULT NULL,
  `update_date` varchar(225) DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_order_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_user`;
CREATE TABLE `tb_order_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_code` varchar(40) DEFAULT NULL,
  `ticket` varchar(40) DEFAULT NULL,
  `product_code` varchar(20) DEFAULT NULL,
  `long_short` int(11) DEFAULT NULL,
  `hand_number` double DEFAULT NULL,
  `open_price` double DEFAULT NULL,
  `close_price` double DEFAULT NULL,
  `open_time` varchar(80) DEFAULT NULL,
  `close_time` varchar(80) DEFAULT NULL,
  `profit` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `plat_form_code` varchar(20) DEFAULT NULL,
  `agency_name` varchar(80) DEFAULT NULL,
  `create_date` varchar(225) DEFAULT NULL,
  `update_date` varchar(225) DEFAULT NULL,
  `create_user` bigint(225) DEFAULT NULL,
  `update_by_user` bigint(20) DEFAULT NULL,
  `commission` double DEFAULT NULL,
  `stop_profit` double DEFAULT NULL,
  `stop_loss` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2496 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_platform
-- ----------------------------
DROP TABLE IF EXISTS `tb_platform`;
CREATE TABLE `tb_platform` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `order_by` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user` varchar(255) NOT NULL,
  `modify_time` datetime DEFAULT NULL,
  `modify_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_useless_ticket
-- ----------------------------
DROP TABLE IF EXISTS `tb_useless_ticket`;
CREATE TABLE `tb_useless_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ticket` varchar(255) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `update_date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_variety
-- ----------------------------
DROP TABLE IF EXISTS `tb_variety`;
CREATE TABLE `tb_variety` (
  `id` bigint(20) NOT NULL,
  `variety_name` varchar(255) NOT NULL COMMENT '品种名称',
  `variety_code` varchar(255) NOT NULL COMMENT '品种代码',
  `trade_place_name` varchar(255) NOT NULL COMMENT '交易所名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users75
-- ----------------------------
DROP TABLE IF EXISTS `users75`;
CREATE TABLE `users75` (
  `LOGIN` bigint(20) DEFAULT NULL,
  `REGDATE` varchar(255) DEFAULT NULL,
  `AGENT` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DEPOSIT` double DEFAULT NULL,
  `WITHDRAWAL` double DEFAULT NULL,
  `BALANCE` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users76
-- ----------------------------
DROP TABLE IF EXISTS `users76`;
CREATE TABLE `users76` (
  `LOGIN` varchar(255) DEFAULT NULL,
  `REGDATE` varchar(255) DEFAULT NULL,
  `AGENT` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DEPOSIT` double DEFAULT NULL,
  `WITHDRAWAL` double DEFAULT NULL,
  `BALANCE` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
