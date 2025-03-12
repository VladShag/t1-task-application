package t1.homework.task_app.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.dto.TaskStatusChangeDto;
import t1.homework.task_app.model.Task;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Модульные тесты класса {@link TaskMapper}.
 */
public class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    @DisplayName("Преобразование ДТО в сущность")
    public void toEntity() {
        TaskRecordDto taskRecordDto = new TaskRecordDto();
        taskRecordDto.setId(1L);
        taskRecordDto.setTitle("Test Title");
        taskRecordDto.setStatus("Testing");

        Task task = taskMapper.toEntity(taskRecordDto);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isNull();
        assertThat(task.getTitle()).isEqualTo(taskRecordDto.getTitle());
        assertThat(task.getStatus()).isEqualTo(taskRecordDto.getStatus());
    }

    @Test
    @DisplayName("Обновление сущности данными из ДТО")
    public void updateFromDto() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setStatus("Testing");

        TaskRecordDto taskRecordDto = new TaskRecordDto();
        taskRecordDto.setTitle("Another Test Title");
        taskRecordDto.setStatus("Another Testing");

        taskMapper.updateFromDto(task, taskRecordDto);

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(taskRecordDto.getTitle());
        assertThat(task.getStatus()).isEqualTo(taskRecordDto.getStatus());
    }

    @Test
    @DisplayName("Преобразование сущности в ДТО")
    public void toDto() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setStatus("Testing");

        TaskRecordDto taskRecordDto = taskMapper.toDto(task);

        assertThat(taskRecordDto).isNotNull();
        assertThat(taskRecordDto.getId()).isEqualTo(task.getId());
        assertThat(taskRecordDto.getTitle()).isEqualTo(task.getTitle());
        assertThat(taskRecordDto.getStatus()).isEqualTo(task.getStatus());
    }

    @Test
    @DisplayName("Преобразование сущности в ДТО смены статуса")
    public void toTaskStatusChangeDto() {
        Task task = new Task();
        String subject = "Test Subject";

        TaskStatusChangeDto taskStatusChangeDto = taskMapper.toTaskStatusChangeDto(task, subject);

        assertThat(taskStatusChangeDto).isNotNull();
        assertThat(taskStatusChangeDto.getTaskId()).isEqualTo(task.getId());
        assertThat(taskStatusChangeDto.getNewStatus()).isEqualTo(task.getStatus());
        assertThat(taskStatusChangeDto.getSubject()).isEqualTo(subject);
    }

    @Test
    @DisplayName("Преобразование сущности в ДТО списком")
    public void toDtoList() {
        Task firstTask = new Task();
        firstTask.setId(1L);
        firstTask.setTitle("Test Title");
        firstTask.setStatus("Testing");

        Task secondTask = new Task();
        secondTask.setId(2L);
        secondTask.setTitle("Another Test Title");
        secondTask.setStatus("Another Testing");

        List<Task> tasks = List.of(firstTask, secondTask);

        List<TaskRecordDto> taskRecordDtos = taskMapper.toDtoList(tasks);
        TaskRecordDto firstDto = taskRecordDtos.get(0);
        TaskRecordDto secondDto = taskRecordDtos.get(1);

        assertThat(taskRecordDtos).isNotNull();
        assertThat(taskRecordDtos).hasSize(2);

        assertThat(firstDto.getId()).isEqualTo(firstTask.getId());
        assertThat(firstDto.getTitle()).isEqualTo(firstTask.getTitle());
        assertThat(firstDto.getStatus()).isEqualTo(firstTask.getStatus());

        assertThat(secondDto.getId()).isEqualTo(secondTask.getId());
        assertThat(secondDto.getTitle()).isEqualTo(secondTask.getTitle());
        assertThat(secondDto.getStatus()).isEqualTo(secondTask.getStatus());
    }
}