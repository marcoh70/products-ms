package com.app.ed.products.service;

import com.app.ed.core.model.ProductCreatedEvent;
import com.app.ed.products.exceptions.KafkaSendMessageException;
import com.app.ed.products.model.ProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService{

    @Value("${topics.producer.products}")
    private String productTopic;

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public String createProductEventAsync(ProductRequest productRequest) {
        String productId = UUID.randomUUID().toString();

        // TODO: Persist to database before publishing the event
        ProductCreatedEvent productCreatedEvent =
                ProductCreatedEvent.builder()
                        .id(productId)
                        .title(productRequest.getTitle())
                        .price(productRequest.getPrice())
                        .quantity(productRequest.getQuantity())
                        .build();
        // Creating the ProducerRecord object to send unique id in headers
        ProducerRecord<String, ProductCreatedEvent> producerRecord =
                new ProducerRecord<>(
                        productTopic,
                        productCreatedEvent
                );
        producerRecord.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        // Sending message async
        kafkaTemplate.send(producerRecord)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Failed to send message [ topic:{}, error:{} ]", productTopic, exception.getMessage());
                    } else {
                        log.info("Message send successfully {}", result.getRecordMetadata());
                    }
                });
        return productId;
    }

    public String createProductEventSync(ProductRequest productRequest) {
        String productId = UUID.randomUUID().toString();

        // TODO: Persist to database before publishing the event
        ProductCreatedEvent productCreatedEvent =
                ProductCreatedEvent.builder()
                        .id(productId)
                        .title(productRequest.getTitle())
                        .price(productRequest.getPrice())
                        .quantity(productRequest.getQuantity())
                        .build();
        // Sending message async
        SendResult<String, ProductCreatedEvent> result = null;
        try {
            result = kafkaTemplate.send(productTopic, productId, productCreatedEvent).get();

        } catch (Exception e) {
            log.error("Error: [{}] sending message to topic: {}", e.getMessage(), productTopic);
            throw new KafkaSendMessageException("Error: " + e.getMessage() + "sending message to topic: " + productTopic);
        }
        return productId;
    }
}
