package t1.homework.task_app.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.dto.TaskStatusChangeDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Модульные тесты класса {@link NotificationService}.
 */
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    private static final Long DEFAULT_ID = 1L;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private NotificationService notificationService;

    private TaskStatusChangeDto taskStatusChangeDto;
    private TaskRecordDto taskDto;
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        taskStatusChangeDto = new TaskStatusChangeDto();
        taskStatusChangeDto.setTaskId(DEFAULT_ID);
        taskStatusChangeDto.setSubject("Task Status Update");
        taskStatusChangeDto.setNewStatus("COMPLETED");

        taskDto = new TaskRecordDto();
        taskDto.setId(DEFAULT_ID);
        taskDto.setTitle("Test Task");
        taskDto.setEmail("test@example.com");

        mimeMessage = mock(MimeMessage.class);
    }

    @Test
    @DisplayName("Корректная отработка отправки сообщения о смене статуса задачи")
    void sendStatusChangeNotification_success() {
        when(taskService.getTaskById(DEFAULT_ID)).thenReturn(taskDto);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.sendStatusChangeNotification(taskStatusChangeDto);

        verify(taskService, times(1)).getTaskById(DEFAULT_ID);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Отработка при не найденном email пользователя - отправка не происходит")
    void sendStatusChangeNotification_noEmail_notSendingMessage() {
        taskDto.setEmail(null);
        when(taskService.getTaskById(DEFAULT_ID)).thenReturn(taskDto);

        notificationService.sendStatusChangeNotification(taskStatusChangeDto);

        verify(taskService, times(1)).getTaskById(DEFAULT_ID);
        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}