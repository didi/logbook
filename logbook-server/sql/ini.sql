# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.17)
# Database: logbook
# Generation Time: 2021-11-23 14:52:55 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table mapping_consume_rule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mapping_consume_rule`;

CREATE TABLE `mapping_consume_rule` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `consumer_group_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费组名称',
  `filter_type` int(8) NOT NULL DEFAULT '1' COMMENT '过滤规则：1=groovy',
  `filter_script` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '过滤脚本，filter_type为1时、这里对应的就是 groovy 代码',
  `dispatch_destination` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分发目标：dispatch_type 为1时、这里对应的就是 kafka topic name',
  `comment` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_consumer_group_name` (`consumer_group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消费者枚举表';

LOCK TABLES `mapping_consume_rule` WRITE;
/*!40000 ALTER TABLE `mapping_consume_rule` DISABLE KEYS */;

INSERT INTO `mapping_consume_rule` (`id`, `consumer_group_name`, `filter_type`, `filter_script`, `dispatch_destination`, `comment`, `status`)
VALUES
	(50000,'demo_group',1,'','dest1','原文透传',1);

/*!40000 ALTER TABLE `mapping_consume_rule` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mapping_entity_produce
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mapping_entity_produce`;

CREATE TABLE `mapping_entity_produce` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `entity_type_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '实体类型id',
  `topic_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'topic名称',
  `comment` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_entity_type_id` (`entity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='实体类型与topic的对应关系';

LOCK TABLES `mapping_entity_produce` WRITE;
/*!40000 ALTER TABLE `mapping_entity_produce` DISABLE KEYS */;

INSERT INTO `mapping_entity_produce` (`id`, `entity_type_id`, `topic_name`, `comment`, `status`)
VALUES
	(1,30000,'test','工单实体对应的topic配置',1),
	(2,30001,'test','坐席实体对应的topic配置',1);

/*!40000 ALTER TABLE `mapping_entity_produce` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mapping_permit_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mapping_permit_config`;

CREATE TABLE `mapping_permit_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `biz_event_type_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务事件id',
  `permit_type` int(8) NOT NULL DEFAULT '1' COMMENT '准入方案类型：1=jsonSchema',
  `permit_context` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '准入上下文配置',
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_biz_event_type_id` (`biz_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务事件准入配置';

LOCK TABLES `mapping_permit_config` WRITE;
/*!40000 ALTER TABLE `mapping_permit_config` DISABLE KEYS */;

INSERT INTO `mapping_permit_config` (`id`, `biz_event_type_id`, `permit_type`, `permit_context`, `comment`, `status`)
VALUES
	(1,20000,1,'{\"$schema\":\"https://json-schema.org/draft/2019-09/schema\",\"type\":\"object\",\"properties\":{\"bizEntityInfo\":{\"type\":\"object\",\"properties\":{\"worksheet_info\":{\"type\":\"object\",\"properties\":{\"worksheet_id\":{\"type\":\"integer\"},\"user_id\":{\"type\":\"integer\"},\"create_time\":{\"type\":\"string\"},\"group_id\":{\"type\":\"integer\"}},\"required\":[\"worksheet_id\",\"user_id\",\"create_time\"]}},\"required\":[\"worksheet_info\"]},\"bizEntityInfoBefore\":{\"type\":\"object\",\"properties\":{\"worksheet_info\":{\"type\":\"object\",\"properties\":{\"worksheet_id\":{\"type\":\"integer\"}}}},\"required\":[\"worksheet_info\"]},\"bizEventInfo\":{\"type\":\"object\",\"properties\":{\"event_time\":{\"type\":\"integer\"},\"operate_kefu_user_id\":{\"type\":\"integer\"}},\"required\":[\"event_time\",\"operate_kefu_user_id\"]},\"bizEventType\":{\"type\":\"number\"},\"bizEventId\":{\"type\":\"string\"}},\"required\":[\"bizEventType\",\"bizEventInfo\",\"bizEntityInfo\",\"bizEventId\"]}','创建工单 jsonSchema 校验',1);

/*!40000 ALTER TABLE `mapping_permit_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mapping_translate_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mapping_translate_config`;

CREATE TABLE `mapping_translate_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `biz_event_type_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务事件id',
  `translate_type` int(8) NOT NULL DEFAULT '1' COMMENT '翻译方案类型：1=groovy',
  `translate_context` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '翻译上下文配置',
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  KEY `idx_biz_event_type_id` (`biz_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务事件翻译配置';

LOCK TABLES `mapping_translate_config` WRITE;
/*!40000 ALTER TABLE `mapping_translate_config` DISABLE KEYS */;

INSERT INTO `mapping_translate_config` (`id`, `biz_event_type_id`, `translate_type`, `translate_context`, `comment`, `status`)
VALUES
	(1,20000,1,'import com.didiglobal.common.translate.TranslateOutput;\nTranslateOutput output = new TranslateOutput();\noutput.setEventType(40000);\noutput.setEventInfo(translateInput.getBizEventInfo());\nLong eventTime = translateInput.getBizEventInfo().getLong(\"event_time\");\noutput.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);\noutput.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getLong(\"worksheet_id\")));\noutput.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore());\noutput.setEntityInfoAfterEvent(translateInput.getBizEntityInfo());\nreturn output;\n\n','创建工单事件翻译为：工单被创建事件',1);

/*!40000 ALTER TABLE `mapping_translate_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_biz_event_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_biz_event_type`;

CREATE TABLE `meta_biz_event_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '业务事件类型id（别名：biz_event_type_id），自动生成全局唯一',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务事件名称（biz_event_name）',
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务事件枚举表';

LOCK TABLES `meta_biz_event_type` WRITE;
/*!40000 ALTER TABLE `meta_biz_event_type` DISABLE KEYS */;

INSERT INTO `meta_biz_event_type` (`id`, `name`, `comment`, `status`)
VALUES
	(20000,'创建工单','',1),
	(20001,'关闭工单','',1);

/*!40000 ALTER TABLE `meta_biz_event_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_caller
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_caller`;

CREATE TABLE `meta_caller` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `caller_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '调用方唯一标识',
  `caller_credential` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '与调用方约定的密钥',
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_caller_name` (`caller_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='调用方枚举表';

LOCK TABLES `meta_caller` WRITE;
/*!40000 ALTER TABLE `meta_caller` DISABLE KEYS */;

INSERT INTO `meta_caller` (`id`, `caller_name`, `caller_credential`, `comment`, `status`)
VALUES
	(10000,'test','123456','',1),
	(10001,'test1','123456','',1);

/*!40000 ALTER TABLE `meta_caller` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_consumer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_consumer`;

CREATE TABLE `meta_consumer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `consumer_group_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费组名称',
  `consumer_context` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消费者上下文配置',
  `comment` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_consumer_group_name` (`consumer_group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消费者枚举表';

LOCK TABLES `meta_consumer` WRITE;
/*!40000 ALTER TABLE `meta_consumer` DISABLE KEYS */;

INSERT INTO `meta_consumer` (`id`, `consumer_group_name`, `consumer_context`, `comment`, `status`)
VALUES
	(50000,'demo_group','{\"key.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\",\"auto.offset.reset\":\"earliest\",\"customize.consumer.topics\":\"test\",\"bootstrap.servers\":\"10.96.76.98:9092\",\"enable.auto.commit\":\"true\",\"group.id\":\"demo-group\",\"value.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\"}','消费组',1),
	(50001,'demo_group2','{\"key.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\",\"auto.offset.reset\":\"earliest\",\"customize.consumer.topics\":\"test\",\"bootstrap.servers\":\"10.96.76.98:9092\",\"enable.auto.commit\":\"true\",\"group.id\":\"demo-group-2\",\"value.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\"}','第二个消费组',0);

/*!40000 ALTER TABLE `meta_consumer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_entity_event_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_entity_event_type`;

CREATE TABLE `meta_entity_event_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '实体事件类型id（别名：entity_event_type_id），自动生成全局唯一',
  `entity_type_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '实体类型id',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '实体事件类型名称',
  `comment` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  KEY `idx_entity_type_id` (`entity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='实体事件类型枚举表';

LOCK TABLES `meta_entity_event_type` WRITE;
/*!40000 ALTER TABLE `meta_entity_event_type` DISABLE KEYS */;

INSERT INTO `meta_entity_event_type` (`id`, `entity_type_id`, `name`, `comment`, `status`)
VALUES
	(40000,30000,'工单实体-被创建事件',NULL,1),
	(40001,30000,'工单实体-被关闭事件',NULL,1),
	(40002,30001,'坐席实体-创建工单事件',NULL,1),
	(40003,30001,'坐席实体-关闭工单事件',NULL,1);

/*!40000 ALTER TABLE `meta_entity_event_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_entity_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_entity_type`;

CREATE TABLE `meta_entity_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '实体类型id（别名：entity_type_id），自动生成全局唯一',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '实体类型名称',
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='实体类型枚举表';

LOCK TABLES `meta_entity_type` WRITE;
/*!40000 ALTER TABLE `meta_entity_type` DISABLE KEYS */;

INSERT INTO `meta_entity_type` (`id`, `name`, `comment`, `status`)
VALUES
	(30000,'工单','工单实体',1),
	(30001,'坐席','坐席实体',1);

/*!40000 ALTER TABLE `meta_entity_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_producer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_producer`;

CREATE TABLE `meta_producer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `topic_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '生产者topic名称',
  `topic_context` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '生产者上下文配置',
  `comment` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_topic_name` (`topic_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='生产者枚举表';

LOCK TABLES `meta_producer` WRITE;
/*!40000 ALTER TABLE `meta_producer` DISABLE KEYS */;

INSERT INTO `meta_producer` (`id`, `topic_name`, `topic_context`, `comment`, `status`)
VALUES
	(50000,'test','{\"bootstrap.servers\":\"10.96.76.98:9092\",\"value.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"key.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\"}','kafka topic 配置',1),
	(50001,'dest1','{\"bootstrap.servers\":\"10.96.76.98:9092\",\"value.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"key.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\"}','dest1配置',1);

/*!40000 ALTER TABLE `meta_producer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table record_biz_event_progress
# ------------------------------------------------------------

DROP TABLE IF EXISTS `record_biz_event_progress`;

CREATE TABLE `record_biz_event_progress` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `global_biz_event_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '上报消息唯一id',
  `status` int(11) NOT NULL DEFAULT '-1' COMMENT '上报消息处理状态.0-未处理, 10-处理完成',
  `caller_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据上报系统',
  `trace_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'trace_id',
  `span_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'span_id',
  `biz_event_body` longtext COLLATE utf8mb4_unicode_ci COMMENT '上报消息的完整内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `success_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '翻译同步成功时间',
  `abort_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '作废时间',
  PRIMARY KEY (`id`,`create_time`),
  UNIQUE KEY `uniq_global_biz_event_id` (`global_biz_event_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上报消息去重表';



# Dump of table record_entity_event_progress
# ------------------------------------------------------------

DROP TABLE IF EXISTS `record_entity_event_progress`;

CREATE TABLE `record_entity_event_progress` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `consume_plugin_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费组名称',
  `consume_global_event_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费消息唯一id',
  `consume_message_context` longtext COLLATE utf8mb4_unicode_ci COMMENT '消费到的消息上下文',
  `status` int(11) NOT NULL DEFAULT '-1' COMMENT '消息分发状态。0-未分发，1-处理中，10-处理完成',
  `trace_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'trace_id',
  `span_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'span_id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `success_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '分发成功时间',
  `abort_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '作废时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_consume_global_event_id` (`consume_global_event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息分发去重表';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
