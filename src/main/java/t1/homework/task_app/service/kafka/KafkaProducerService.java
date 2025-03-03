package t1.homework.task_app.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import t1.homework.task_app.dto.TaskStatusChangeDto;

/**
 * Сервис отправки сообщений в кафку.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, TaskStatusChangeDto> kafkaTemplate;

    @Value("${kafka.topic.task-status-change.name}")
    private String taskStatusChangeTopic;

    /**
     * Отправить сообщение в кафку о смене статуса задачи.
     *
     * @param taskStatusChange ДТО смены статуса
     */
    public void sendTaskStatusChange(TaskStatusChangeDto taskStatusChange) {
        log.info("Происходит отправка сообщения {} в топик {}", taskStatusChange, taskStatusChangeTopic);
        kafkaTemplate.send(taskStatusChangeTopic, taskStatusChange.getTaskId().toString(), taskStatusChange);
    }
}