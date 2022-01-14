# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.17)
# Database: logbook
# Generation Time: 2021-12-14 09:09:36 +0000
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

LOCK TABLES `mapping_consume_rule` WRITE;
/*!40000 ALTER TABLE `mapping_consume_rule` DISABLE KEYS */;

INSERT INTO `mapping_consume_rule` (`id`, `consumer_group_name`, `filter_type`, `filter_script`, `dispatch_destination`, `comment`, `status`)
VALUES
	(50000,'consume_downstream1',1,'','destinate_downstream1','downstream1的消费组，消费到的实体事件处理后分发到destinate_downstream1',1),
	(50001,'consume_downstream2',1,'','destinate_downstream2','downstream2的消费组，消费到的实体事件处理后分发到destinate_downstream2',1);

/*!40000 ALTER TABLE `mapping_consume_rule` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mapping_entity_produce
# ------------------------------------------------------------

LOCK TABLES `mapping_entity_produce` WRITE;
/*!40000 ALTER TABLE `mapping_entity_produce` DISABLE KEYS */;

INSERT INTO `mapping_entity_produce` (`id`, `entity_type_id`, `topic_name`, `comment`, `status`)
VALUES
	(1,30000,'inner_worksheet_events','翻译得到的工单实体事件使用的内部队列名称',1),
	(2,30001,'inner_user_events','翻译得到的坐席实体事件使用的内部队列名称',1);

/*!40000 ALTER TABLE `mapping_entity_produce` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mapping_permit_config
# ------------------------------------------------------------

LOCK TABLES `mapping_permit_config` WRITE;
/*!40000 ALTER TABLE `mapping_permit_config` DISABLE KEYS */;

INSERT INTO `mapping_permit_config` (`id`, `biz_event_type_id`, `permit_type`, `permit_context`, `comment`, `status`)
VALUES
	(1,20000,1,'{\"$schema\":\"https://json-schema.org/draft/2019-09/schema\",\"type\":\"object\",\"properties\":{\"biz_entity_info\":{\"type\":\"object\",\"properties\":{\"worksheet_info\":{\"type\":\"object\",\"properties\":{\"worksheet_id\":{\"type\":\"integer\"},\"user_id\":{\"type\":\"integer\"},\"create_time\":{\"type\":\"string\"},\"group_id\":{\"type\":\"integer\"}},\"required\":[\"worksheet_id\",\"user_id\",\"create_time\"]}},\"required\":[\"worksheet_info\"]},\"biz_entity_info_before\":{\"type\":\"object\",\"properties\":{\"worksheet_info\":{\"type\":\"object\",\"properties\":{\"worksheet_id\":{\"type\":\"integer\"}}}},\"required\":[\"worksheet_info\"]},\"biz_event_info\":{\"type\":\"object\",\"properties\":{\"event_time\":{\"type\":\"integer\"},\"user_id\":{\"type\":\"integer\"}},\"required\":[\"event_time\",\"user_id\"]},\"biz_event_type\":{\"type\":\"number\"},\"biz_event_id\":{\"type\":\"string\"}},\"required\":[\"biz_event_type\",\"biz_event_info\",\"biz_entity_info\",\"biz_event_id\"]}','创建工单 jsonSchema校验',1),
	(3,20001,1,'{\"$schema\":\"https://json-schema.org/draft/2019-09/schema\",\"type\":\"object\",\"properties\":{\"biz_entity_info\":{\"type\":\"object\",\"properties\":{\"worksheet_info\":{\"type\":\"object\",\"properties\":{\"worksheet_id\":{\"type\":\"integer\"},\"user_id\":{\"type\":\"integer\"},\"create_time\":{\"type\":\"string\"},\"group_id\":{\"type\":\"integer\"}},\"required\":[\"worksheet_id\",\"user_id\",\"create_time\"]}},\"required\":[\"worksheet_info\"]},\"biz_entity_info_before\":{\"type\":\"object\",\"properties\":{\"worksheet_info\":{\"type\":\"object\",\"properties\":{\"worksheet_id\":{\"type\":\"integer\"}}}},\"required\":[\"worksheet_info\"]},\"biz_event_info\":{\"type\":\"object\",\"properties\":{\"event_time\":{\"type\":\"integer\"},\"user_id\":{\"type\":\"integer\"}},\"required\":[\"event_time\",\"user_id\"]},\"biz_event_type\":{\"type\":\"number\"},\"biz_event_id\":{\"type\":\"string\"}},\"required\":[\"biz_event_type\",\"biz_event_info\",\"biz_entity_info\",\"biz_event_id\"]}','关闭工单 jsonSchema校验',1),
	(4,20002,1,'{\"$schema\":\"https://json-schema.org/draft/2019-09/schema\",\"type\":\"object\",\"properties\":{\"biz_entity_info\":{\"type\":\"object\",\"properties\":{\"user_info\":{\"type\":\"object\",\"properties\":{\"user_id\":{\"type\":\"integer\"},\"user_name\":{\"type\":\"string\"},\"register_time\":{\"type\":\"string\"},\"group_id\":{\"type\":\"integer\"}},\"required\":[\"user_id\",\"user_name\",\"group_id\"]}},\"required\":[\"user_info\"]},\"biz_entity_info_before\":{\"type\":\"object\",\"properties\":{\"user_info\":{\"type\":\"object\",\"properties\":{\"user_id\":{\"type\":\"integer\"}}}},\"required\":[\"user_info\"]},\"biz_event_info\":{\"type\":\"object\",\"properties\":{\"event_time\":{\"type\":\"integer\"},\"associate_system_name\":{\"type\":\"string\"}},\"required\":[\"event_time\",\"associate_system_name\"]},\"biz_event_type\":{\"type\":\"number\"},\"biz_event_id\":{\"type\":\"string\"}},\"required\":[\"biz_event_type\",\"biz_event_info\",\"biz_entity_info\",\"biz_event_id\"]}','坐席登录jsonSchema校验',1),
	(5,20003,1,'{\"$schema\":\"https://json-schema.org/draft/2019-09/schema\",\"type\":\"object\",\"properties\":{\"biz_entity_info\":{\"type\":\"object\",\"properties\":{\"user_info\":{\"type\":\"object\",\"properties\":{\"user_id\":{\"type\":\"integer\"},\"user_name\":{\"type\":\"string\"},\"register_time\":{\"type\":\"string\"},\"group_id\":{\"type\":\"integer\"}},\"required\":[\"user_id\",\"user_name\",\"group_id\"]}},\"required\":[\"user_info\"]},\"biz_entity_info_before\":{\"type\":\"object\",\"properties\":{\"user_info\":{\"type\":\"object\",\"properties\":{\"user_id\":{\"type\":\"integer\"}}}},\"required\":[\"user_info\"]},\"biz_event_info\":{\"type\":\"object\",\"properties\":{\"event_time\":{\"type\":\"integer\"},\"associate_system_name\":{\"type\":\"string\"}},\"required\":[\"event_time\",\"associate_system_name\"]},\"biz_event_type\":{\"type\":\"number\"},\"biz_event_id\":{\"type\":\"string\"}},\"required\":[\"biz_event_type\",\"biz_event_info\",\"biz_entity_info\",\"biz_event_id\"]}','坐席登出jsonSchema校验',1);

/*!40000 ALTER TABLE `mapping_permit_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mapping_translate_config
# ------------------------------------------------------------

LOCK TABLES `mapping_translate_config` WRITE;
/*!40000 ALTER TABLE `mapping_translate_config` DISABLE KEYS */;

INSERT INTO `mapping_translate_config` (`id`, `biz_event_type_id`, `translate_type`, `translate_context`, `comment`, `status`)
VALUES
	(1,20000,1,'import com.didiglobal.common.translate.TranslateOutput;\nTranslateOutput output = new TranslateOutput();\noutput.setEventType(40000);\noutput.setEventInfo(translateInput.getBizEventInfo());\nLong eventTime = translateInput.getBizEventInfo().getLong(\"event_time\");\noutput.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);\noutput.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getJSONObject(\"worksheet_info\").getLong(\"worksheet_id\")));\noutput.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore().getJSONObject(\"worksheet_info\"));\noutput.setEntityInfoAfterEvent(translateInput.getBizEntityInfo().getJSONObject(\"worksheet_info\"));\nreturn output;\n\n','创建工单事件翻译为：工单被创建事件',1),
	(2,20001,1,'import com.didiglobal.common.translate.TranslateOutput;\nTranslateOutput output = new TranslateOutput();\noutput.setEventType(40001);\noutput.setEventInfo(translateInput.getBizEventInfo());\nLong eventTime = translateInput.getBizEventInfo().getLong(\"event_time\");\noutput.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);\noutput.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getJSONObject(\"worksheet_info\").getLong(\"worksheet_id\")));\noutput.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore(). getJSONObject(\"worksheet_info\"));\noutput.setEntityInfoAfterEvent(translateInput.getBizEntityInfo(). getJSONObject(\"worksheet_info\"));\nreturn output;\n\n','关闭工单事件翻译为：工单被关闭事件',1),
	(3,20000,1,'import com.didiglobal.common.translate.TranslateOutput;\nTranslateOutput output = new TranslateOutput();\noutput.setEventType(40002);\noutput.setEventInfo(translateInput.getBizEventInfo());\nLong eventTime = translateInput.getBizEventInfo().getLong(\"event_time\");\noutput.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);\noutput.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getJSONObject(\"user_info\").getLong(\"user_id\")));\noutput.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore().getJSONObject(\"user_info\"));\noutput.setEntityInfoAfterEvent(translateInput.getBizEntityInfo().getJSONObject(\"user_info\"));\nreturn output;\n\n','创建工单事件翻译为：坐席创建工单',1),
	(4,20001,1,'import com.didiglobal.common.translate.TranslateOutput;\nTranslateOutput output = new TranslateOutput();\noutput.setEventType(40003);\noutput.setEventInfo(translateInput.getBizEventInfo());\nLong eventTime = translateInput.getBizEventInfo().getLong(\"event_time\");\noutput.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);\noutput.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getJSONObject(\"user_info\").getLong(\"user_id\")));\noutput.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore().getJSONObject(\"user_info\"));\noutput.setEntityInfoAfterEvent(translateInput.getBizEntityInfo().getJSONObject(\"user_info\"));\nreturn output;\n\n','关闭工单事件翻译为：坐席关闭工单',1),
	(5,20002,1,'import com.didiglobal.common.translate.TranslateOutput;\nTranslateOutput output = new TranslateOutput();\noutput.setEventType(40004);\noutput.setEventInfo(translateInput.getBizEventInfo());\nLong eventTime = translateInput.getBizEventInfo().getLong(\"event_time\");\noutput.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);\noutput.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getJSONObject(\"user_info\").getLong(\"user_id\")));\noutput.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore().getJSONObject(\"user_info\"));\noutput.setEntityInfoAfterEvent(translateInput.getBizEntityInfo().getJSONObject(\"user_info\"));\nreturn output;\n\n','坐席登录事件被翻译为：坐席登录系统',1),
	(6,20003,1,'import com.didiglobal.common.translate.TranslateOutput;\nTranslateOutput output = new TranslateOutput();\noutput.setEventType(40005);\noutput.setEventInfo(translateInput.getBizEventInfo());\nLong eventTime = translateInput.getBizEventInfo().getLong(\"event_time\");\noutput.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);\noutput.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getJSONObject(\"user_info\").getLong(\"user_id\")));\noutput.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore().getJSONObject(\"user_info\"));\noutput.setEntityInfoAfterEvent(translateInput.getBizEntityInfo().getJSONObject(\"user_info\"));\nreturn output;\n\n','坐席登出事件被翻译为：坐席登出系统',1);

/*!40000 ALTER TABLE `mapping_translate_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_biz_event_type
# ------------------------------------------------------------

LOCK TABLES `meta_biz_event_type` WRITE;
/*!40000 ALTER TABLE `meta_biz_event_type` DISABLE KEYS */;

INSERT INTO `meta_biz_event_type` (`id`, `name`, `comment`, `status`)
VALUES
	(20000,'创建工单','',1),
	(20001,'关闭工单','',1),
	(20002,'坐席登录','',1),
	(20003,'坐席登出','',1);

/*!40000 ALTER TABLE `meta_biz_event_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_caller
# ------------------------------------------------------------

LOCK TABLES `meta_caller` WRITE;
/*!40000 ALTER TABLE `meta_caller` DISABLE KEYS */;

INSERT INTO `meta_caller` (`id`, `caller_name`, `caller_credential`, `comment`, `status`)
VALUES
	(10000,'upstream1','123456','名为upstream1的上游系统',1);

/*!40000 ALTER TABLE `meta_caller` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_consumer
# ------------------------------------------------------------

LOCK TABLES `meta_consumer` WRITE;
/*!40000 ALTER TABLE `meta_consumer` DISABLE KEYS */;

INSERT INTO `meta_consumer` (`id`, `consumer_group_name`, `consumer_context`, `comment`, `status`)
VALUES
	(50000,'consume_downstream1','{\"key.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\",\"auto.offset.reset\":\"earliest\",\"customize.consumer.topics\":\"inner_worksheet_events\",\"bootstrap.servers\":\"你的kafka地址\",\"enable.auto.commit\":\"true\",\"group.id\":\"consume_downstream1\",\"value.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\"}','为第一个数据使用方创建的消费插件/消费组，消费内部工单实体事件队列',1),
	(50001,'consume_downstream2','{\"key.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\",\"auto.offset.reset\":\"earliest\",\"customize.consumer.topics\":\"inner_user_events\",\"bootstrap.servers\":\"你的kafka地址\",\"enable.auto.commit\":\"true\",\"group.id\":\"consume_downstream2\",\"value.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\"}','为第二个数据使用方创建的消费插件/消费组，消费内部坐席实体事件队列',1);

/*!40000 ALTER TABLE `meta_consumer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_entity_event_type
# ------------------------------------------------------------

LOCK TABLES `meta_entity_event_type` WRITE;
/*!40000 ALTER TABLE `meta_entity_event_type` DISABLE KEYS */;

INSERT INTO `meta_entity_event_type` (`id`, `entity_type_id`, `name`, `comment`, `status`)
VALUES
	(40000,30000,'工单实体-被创建事件','',1),
	(40001,30000,'工单实体-被关闭事件','',1),
	(40002,30001,'坐席实体-创建工单事件','',1),
	(40003,30001,'坐席实体-关闭工单事件','',1),
	(40004,30001,'坐席实体-登录系统事件','',1),
	(40005,30001,'坐席实体-登出系统事件','',1);

/*!40000 ALTER TABLE `meta_entity_event_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta_entity_type
# ------------------------------------------------------------

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

LOCK TABLES `meta_producer` WRITE;
/*!40000 ALTER TABLE `meta_producer` DISABLE KEYS */;

INSERT INTO `meta_producer` (`id`, `topic_name`, `topic_context`, `comment`, `status`)
VALUES
	(50000,'inner_worksheet_events','{\"bootstrap.servers\":\"你的kafka地址\",\"value.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"key.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\"}','内部工单事件队列',1),
	(50001,'inner_user_events','{\"bootstrap.servers\":\"你的kafka地址\",\"value.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"key.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\"}','内部坐席事件队列',1),
	(50002,'destinate_downstream1','{\"bootstrap.servers\":\"你的kafka地址\",\"value.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"key.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\"}','分发给downstream1的事件队列',1),
	(50003,'destinate_downstream2','{\"bootstrap.servers\":\"你的kafka地址\",\"value.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"key.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\"}','分发给downstream2的事件队列',1);

/*!40000 ALTER TABLE `meta_producer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table record_biz_event_progress
# ------------------------------------------------------------



# Dump of table record_entity_event_progress
# ------------------------------------------------------------




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
