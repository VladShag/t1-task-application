package t1.homework.task_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import t1.homework.task_app.dto.TaskDto;
import t1.homework.task_app.model.Task;

import java.util.List;

/**
 * Маппер для работы с {@link Task}.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDto taskDto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(@MappingTarget Task task, TaskDto taskDto);

    TaskDto toDto(Task task);

    List<TaskDto> toDtoList(List<Task> task);
}