package com.app.ed.products.config;

import com.app.ed.core.model.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class KafkaConfig {

    @Value("${topics.producer.products}")
    private String productsTopic;

    private final ProducerProperties producerProperties;

    private Map<String, Object> producerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.getBootstrapServer());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProperties.getKeySerializer());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerProperties.getValueSerializer());
        configs.put(ProducerConfig.ACKS_CONFIG, producerProperties.getProperties().getAcks());
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, producerProperties.getProperties().getDelivery().getTimeout().getMs());
        configs.put(ProducerConfig.LINGER_MS_CONFIG, producerProperties.getProperties().getLinger().getMs());
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProperties.getProperties().getRequest().getTimeout().getMs());
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerProperties.getProperties().getEnable().isIdempotence());
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, producerProperties.getProperties().getMax().getIn().getFlight().getRequests().getPer().getConnection());
        configs.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        return configs;
    }

    @Bean
    public ProducerFactory<String, ProductCreatedEvent> producerFactory() {
        return new DefaultKafkaProducerFactory(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }

    @Bean
    NewTopic createTopic() {
        return TopicBuilder
                .name(productsTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
