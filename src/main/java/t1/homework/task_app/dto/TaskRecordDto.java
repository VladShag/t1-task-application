package t1.homework.task_app.dto;

import lombok.Data;

/**
 * ДТО задачи.
 */
@Data
public class TaskRecordDto {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}
