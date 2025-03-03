package t1.homework.task_app.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import t1.homework.task_app.dto.TaskStatusChangeDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация продюсеров кафки.
 */
@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServersConfig;

    /**
     * Конфигурация {@link ProducerFactory}
     */
    @Bean
    public ProducerFactory<String, TaskStatusChangeDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Конфигурация {@link KafkaTemplate}
     */
    @Bean
    public KafkaTemplate<String, TaskStatusChangeDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}