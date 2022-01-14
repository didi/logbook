
-- biz_event 记录【done】
CREATE TABLE `record_biz_event_progress` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `global_biz_event_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '上报消息唯一id',
  `status` int(11) NOT NULL DEFAULT '-1' COMMENT '上报消息处理状态.0-未处理, 10-处理完成',
  `caller_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据上报系统',
  `trace_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'trace_id',
  `span_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'span_id',
  `biz_event_body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '上报消息的完整内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `success_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '翻译同步成功时间',
  `abort_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '作废时间',
  PRIMARY KEY (`id`,`create_time`),
  UNIQUE KEY `uniq_global_biz_event_id` (`global_biz_event_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=5862147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上报消息去重表'




-- entity_event 记录【done】
CREATE TABLE `record_entity_event_progress` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，暂时不用',
  `consume_plugin_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费组名称',
  `consume_global_event_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费消息唯一id',
  `consume_message_context` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '消费到的消息上下文',
  `status` int(11) NOT NULL DEFAULT '-1' COMMENT '消息分发状态。0-未分发，1-处理中，10-处理完成',
  `trace_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'trace_id',
  `span_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'span_id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `success_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '分发成功时间',
  `abort_time` timestamp NOT NULL DEFAULT '1971-01-01 08:00:00' COMMENT '作废时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_consume_global_event_id` (`consume_global_event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16823902 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息分发去重表'

