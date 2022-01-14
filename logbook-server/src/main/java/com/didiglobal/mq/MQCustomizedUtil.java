package com.didiglobal.mq;

import com.didiglobal.common.pojo.CommonConsumerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * @author liyanling
 * @date 2022/1/6 5:12 下午
 *
 * 这里面的所有方法都是需要二次开发完成的
 * 注释给出了 kafka-clients 2.8.1 的示例代码
 */
@Slf4j
public class MQCustomizedUtil {
    private final static String CUSTOMIZE_CONSUMER_POLL_DURATION = "customize.consumer.duration.millis";
    private final static String CUSTOMIZE_CONSUMER_TOPICS = "customize.consumer.topics";

    public static Object doCreateProducer(Properties properties) {
        // TODO 二次开发 realityProducer 构建逻辑
        // eg kafka 实现：
        // KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        return null;
    }

    public static void doCloseProducer(Object realityProducer) {
        // TODO 二次开发：realityProducer 关闭逻辑
        // eg kafka 实现：
        // kafkaProducer.close();
    }

    public static void doProduce(Object realityProducer, String topicName, String messageBody) {
        // TODO 二次开发：realityProducer 发送逻辑
        // eg kafka 实现：
        // RecordMetadata metadata =
        // kafkaProducer.send(new ProducerRecord<>(topicName, messageBody)).get(10, TimeUnit.SECONDS);
    }


    public static Object doCreateConsumer(Properties properties) {
        // TODO 二次开发：realityConsumer 生成
        // eg kafka 实现：
        // KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        return null;
    }

    public static void doCloseConsumer(Object realityConsumer) {
        // TODO 二次开发：realityConsumer 关闭逻辑
        // eg kafka 实现：
        // consumer.close();
    }

    public static void doConsumerSubscribe(Properties properties) {
        // TODO 二次开发：realityConsumer 订阅逻辑
        // eg kafka 实现：
        // consumer.subscribe(getTopics());
    }

    public static Map<String, CommonConsumerRecord> doConsumeOnce(Object realityConsumer) {
        // TODO 二次开发：realityConsumer 消费逻辑
        // eg kafka 实现：
        // poll 消息，间隔是在 properties 里用 CUSTOMIZE_CONSUMER_POLL_DURATION 配置的
        // ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(getMillis()));
        // Map<String, CommonConsumerRecord> map = new ConcurrentHashMap<>();
        // for (ConsumerRecord<String, String> record : records) {
        //    map.put(record.key(), CommonConsumerRecord.builder().topicName(record.topic()).value(record.value()).build());
        // }
        // return map;
        return null;
    }

    // 从 properties 里取 topics 配置（逗号分隔）
    private Collection<String> getTopics(Properties properties) {
        String topics = properties.getProperty(CUSTOMIZE_CONSUMER_TOPICS);
        if (topics == null || topics.length() == 0) {
            log.error("getTopics failed,topics is null or empty");
            throw new IllegalArgumentException("'customize.consumer.topics' can not null");
        }
        return Arrays.asList(topics.split(","));
    }

    // 从 properties 里取 poll 间隔时长（默认 1000 millis）
    private long getMillis(Properties properties) {
        String duration = properties.getProperty(CUSTOMIZE_CONSUMER_POLL_DURATION);
        if (duration == null || duration.length() == 0) {
            return 1000;
        }
        try {
            return Long.parseLong(duration);
        } catch (Exception e) {
            return 1000;
        }
    }

}
