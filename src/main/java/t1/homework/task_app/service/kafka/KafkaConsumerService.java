package t1.homework.task_app.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import t1.homework.task_app.dto.TaskStatusChangeDto;
import t1.homework.task_app.service.NotificationService;

import java.util.List;

/**
 * Сервис слушателей кафки.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final NotificationService notificationService;

    @Value("${kafka.topic.task-status-change.name}")
    private String topicName;

    /**
     * Обработать сообщение полученное из топика о смене статуса задачи.
     *
     * @param taskStatusChanges Список ДТО смен статуса
     */
    @KafkaListener(topics = "${kafka.topic.task-status-change.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeTaskStatusChange(List<TaskStatusChangeDto> taskStatusChanges) {
        log.info("Получено {} сообщений из топика {}.", taskStatusChanges.size(), topicName);
        for (TaskStatusChangeDto taskStatusChange : taskStatusChanges) {
            notificationService.sendStatusChangeNotification(taskStatusChange);
        }
    }
}