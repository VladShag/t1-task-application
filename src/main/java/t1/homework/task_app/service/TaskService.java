package t1.homework.task_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vshagalov.logging_starter.annotation.LogAfterReturning;
import ru.vshagalov.logging_starter.annotation.LogAfterThrowing;
import ru.vshagalov.logging_starter.annotation.LogAround;
import ru.vshagalov.logging_starter.annotation.LogBefore;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.dto.TaskStatusChangeDto;
import t1.homework.task_app.exception.TaskBadRequestException;
import t1.homework.task_app.exception.TaskNotFoundException;
import t1.homework.task_app.mapper.TaskMapper;
import t1.homework.task_app.model.Task;
import t1.homework.task_app.repository.TaskRepository;
import t1.homework.task_app.service.kafka.KafkaProducerService;

import java.util.List;

/**
 * Сервис для работы с задачами.
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    private static final String TASK_STATUS_UPDATE_SUBJECT = "Task Status Change Notification";

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaProducerService kafkaProducerService;

    /**
     * Создать новую задачу.
     *
     * @param task ДТО для создания задачи
     * @return сущность {@link Task} созданной задачи
     */
    @LogAround
    public TaskRecordDto createTask(TaskRecordDto task) {
        Task entityToSave = taskMapper.toEntity(task);
        Task savedEntity = taskRepository.save(entityToSave);
        return taskMapper.toDto(savedEntity);
    }

    /**
     * Получить задачу по её идентификатору.
     *
     * @param id ИД задачи
     * @return {@link TaskRecordDto} дто задачи
     */
    @LogAfterReturning
    @LogAfterThrowing
    public TaskRecordDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Не найдена задача с id: " + id));
        return taskMapper.toDto(task);
    }

    /**
     * Получить все доступные задачи.
     *
     * @return список сущностей {@link Task} задач
     */
    @LogAfterReturning
    public List<TaskRecordDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDtoList(tasks);
    }

    /**
     * Изменить задачу по её идентификатору и отправить уведомление об изменении, если статус задачи изменился.
     *
     * @param id ИД задачи
     * @param taskDetails {@link TaskRecordDto} ДТО с данными для изменения задачи
     * @return {@link TaskRecordDto} дто измененной задачи задачи
     */
    @LogAround
    @Transactional
    public TaskRecordDto updateTaskAndSendNotification(Long id, TaskRecordDto taskDetails) {
        if (taskDetails == null) {
            throw new TaskBadRequestException("Не получены данные для обновления задачи");
        }

        Task task = taskRepository.findById(id).orElseThrow();
        taskMapper.updateFromDto(task, taskDetails);
        taskRepository.save(task);

        if (taskDetails.getStatus() != null) {
            TaskStatusChangeDto statusChangeDto = taskMapper.toTaskStatusChangeDto(task, TASK_STATUS_UPDATE_SUBJECT);
            kafkaProducerService.sendTaskStatusChange(statusChangeDto);
        }

        return taskMapper.toDto(task);
    }

    /**
     * Удалить задачу по её идентификатору.
     *
     * @param id ИД задачи
     */
    @LogBefore
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
