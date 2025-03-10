package t1.homework.task_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.dto.TaskStatusChangeDto;
import t1.homework.task_app.model.Task;

import java.util.List;

/**
 * Маппер для работы с {@link Task}.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskRecordDto taskRecordDto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(@MappingTarget Task task, TaskRecordDto taskRecordDto);

    TaskRecordDto toDto(Task task);

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "newStatus", source = "task.status")
    @Mapping(target = "subject", source = "subject")
    TaskStatusChangeDto toTaskStatusChangeDto(Task task, String subject);

    List<TaskRecordDto> toDtoList(List<Task> task);
}