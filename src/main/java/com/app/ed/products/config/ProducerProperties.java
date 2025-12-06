package com.app.ed.products.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class ProducerProperties {


    private List<String> bootstrapServer;
    private String keySerializer;
    private String valueSerializer;
    private Properties Properties;

    @Data
    public static class Properties {
        private String acks;
        private Delivery delivery;
        @Data
        public static class Delivery {
            private Timeout timeout;
            @Data
            public static class Timeout {
                private int ms;
            }
        }
        private Linger linger;
        @Data
        public static class Linger {
            private int ms;
        }
        private Request request;
        @Data
        public static class Request{
            private Timeout timeout;
            @Data
            public static class Timeout{
                private int ms;
            }
        }
        private Enable enable;
        @Data
        public static class Enable {
            private boolean idempotence;
        }
        private Max max;
        @Data
        public static class Max {
            private In in;
            @Data
            public static class In{
                private Flight flight;
                @Data
                public static class Flight {
                    private Requests requests;
                    @Data
                    public static class Requests {
                        private Per per;
                        @Data
                        public static class Per {
                            private int connection;
                        }
                    }
                }
            }
        }
    }

}
