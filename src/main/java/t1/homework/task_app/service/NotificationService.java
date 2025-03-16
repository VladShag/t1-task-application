package t1.homework.task_app.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.vshagalov.logging_starter.annotation.LogAfterThrowing;
import ru.vshagalov.logging_starter.annotation.LogBefore;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.dto.TaskStatusChangeDto;
import t1.homework.task_app.exception.NotificationException;

/**
 * Сервис отправки уведомления.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private static final String TASK_STATUS_UPDATE_TEXT = "У задачи %s обновился статус на: %s";
    private final JavaMailSender mailSender;
    private final TaskService taskService;

    /**
     * Отправить уведомление о смене статуса задачи.
     *
     * @param taskStatusChange ДТО смена статуса
     */
    @LogBefore
    @LogAfterThrowing
    public void sendStatusChangeNotification(TaskStatusChangeDto taskStatusChange) {
        TaskRecordDto task = taskService.getTaskById(taskStatusChange.getTaskId());
        MimeMessage message = mailSender.createMimeMessage();
        if (task.getEmail() == null) {
            log.info("В задаче c id {} не указан email, отправка письма невозможна", task.getId());
            return;
        }

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(task.getEmail());
            helper.setSubject(taskStatusChange.getSubject());
            helper.setText(String.format(TASK_STATUS_UPDATE_TEXT, task.getTitle(), taskStatusChange.getNewStatus()));
        } catch (MessagingException e) {
            throw new NotificationException("Произошла ошибка при попытке отправить уведомление на почту: " + e.getMessage());
        }
        mailSender.send(message);
    }
}
