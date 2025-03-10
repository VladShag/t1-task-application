package t1.homework.task_app.dto;

import lombok.Data;

/**
 * ДТО обновления статуса задачи.
 */
@Data
public class TaskStatusChangeDto {
    private Long taskId;
    private String newStatus;
    private String subject;
}
