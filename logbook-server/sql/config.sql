

-- caller【done】
CREATE TABLE `meta_caller` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
`caller_name` varchar(20) NOT NULL DEFAULT '' COMMENT '调用方唯一标识',
`caller_credential` varchar(20) NOT NULL DEFAULT '' COMMENT '与调用方约定的密钥',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
UNIQUE KEY `uniq_caller_name` (`caller_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='调用方枚举表';

-- 业务事件【done】
CREATE TABLE `meta_biz_event_type` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '业务事件类型id（别名：biz_event_type_id），自动生成全局唯一',
`name` varchar(20) NOT NULL DEFAULT '' COMMENT '业务事件名称（biz_event_name）',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务事件枚举表';

-- 实体【done】
CREATE TABLE `meta_entity_type` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '实体类型id（别名：entity_type_id），自动生成全局唯一',
`name` varchar(20) NOT NULL DEFAULT '' COMMENT '实体类型名称',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='实体类型枚举表';

-- 实体事件【done】
CREATE TABLE `meta_entity_event_type` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '实体事件类型id（别名：entity_event_type_id），自动生成全局唯一',
`entity_type_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '实体类型id',
`name` varchar(20) NOT NULL DEFAULT '' COMMENT '实体事件类型名称',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
KEY `idx_entity_type_id` (`entity_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='实体事件类型枚举表';

-- 生产者【done】
-- todo 生产者分类
CREATE TABLE `meta_producer` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
`topic_name` varchar(128) NOT NULL DEFAULT '' COMMENT '生产者topic名称',
`topic_context` mediumtext NOT NULL COMMENT '生产者上下文配置',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
UNIQUE KEY `uniq_topic_name` (`topic_name`)
) ENGINE=InnoDB AUTO_INCREMENT=50000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='生产者枚举表';


-- 消费者【done】
-- todo 消费者分类
CREATE TABLE `meta_consumer` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
`consumer_group_name` varchar(128) NOT NULL DEFAULT '' COMMENT '消费组名称',
`consumer_context` mediumtext NOT NULL COMMENT '消费者上下文配置',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
UNIQUE KEY `uniq_consumer_group_name` (`consumer_group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=50000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消费者枚举表';




--
-- 生产层配置
--

---- caller : biz_event_type
--CREATE TABLE `caller_biz_event_type_mapping` (
--`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
--`caller_name` varchar(20) NOT NULL DEFAULT '' COMMENT '调用方唯一标识',
--`biz_event_type_id` bigint(20) NOT NULL DEFAULT '' COMMENT '业务事件id',
--`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
--PRIMARY KEY (`id`),
--UNIQUE KEY `uniq_caller_biz_event_type_id` (`caller_name`,`biz_event_type_id`)
--) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='调用方可上报的业务事件类型映射表';

-- biz_event_type : permit【done】
CREATE TABLE `mapping_permit_config` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
`biz_event_type_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '业务事件id',
`permit_type` int(8) NOT NULL DEFAULT '1' COMMENT '准入方案类型：1=jsonSchema',
`permit_context` mediumtext COMMENT '准入上下文配置，permit_type为1时、这里就是对应的 jsonSchema 串',
`comment` varchar(500) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
UNIQUE KEY `uniq_biz_event_type_id` (`biz_event_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务事件准入配置';

-- biz_event_type : translate【done】
CREATE TABLE `mapping_translate_config` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
`biz_event_type_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '业务事件id',
`translate_type` int(8) NOT NULL DEFAULT '1' COMMENT '翻译方案类型：1=groovy',
`translate_context` mediumtext NOT NULL COMMENT '翻译上下文配置，translate_type为1时、这里就是对应的 groovy 代码',
`comment` varchar(500) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
KEY `idx_biz_event_type_id` (`biz_event_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务事件翻译配置';


-- 映射关系 entity_type_id : topic_name【done】
CREATE TABLE `mapping_entity_produce` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
`entity_type_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '实体类型id',
`topic_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'topic名称',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
UNIQUE KEY `uniq_entity_type_id` (`entity_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='实体类型与topic的对应关系';


-- 映射关系：consumer_group_name : filter & dispatch
-- todo dispatch 分类
CREATE TABLE `mapping_consume_rule` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
`consumer_group_name` varchar(128) NOT NULL DEFAULT '' COMMENT '消费组名称',
`filter_type` int(8) NOT NULL DEFAULT '1' COMMENT '过滤规则：1=groovy',
`filter_script` mediumtext NOT NULL COMMENT '过滤脚本，filter_type为1时、这里对应的就是 groovy 代码',
`dispatch_destination` varchar(128) NOT NULL DEFAULT '' COMMENT '分发目标 kafka topic name',
`comment` varchar(128) NOT NULL DEFAULT '' COMMENT '详细说明',
`status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '1-生效，0-未生效',
PRIMARY KEY (`id`),
UNIQUE KEY `uniq_consumer_group_name` (`consumer_group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=50000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消费者枚举表';

