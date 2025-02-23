package t1.homework.task_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import t1.homework.task_app.annotation.LogAfterReturning;
import t1.homework.task_app.annotation.LogAfterThrowing;
import t1.homework.task_app.annotation.LogAround;
import t1.homework.task_app.annotation.LogBefore;
import t1.homework.task_app.dto.TaskDto;
import t1.homework.task_app.exception.TaskNotFoundException;
import t1.homework.task_app.mapper.TaskMapper;
import t1.homework.task_app.model.Task;
import t1.homework.task_app.repository.TaskRepository;

import java.util.List;

/**
 * Сервис для работы с задачами.
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Создать новую задачу.
     *
     * @param task ДТО для создания задачи
     * @return сущность {@link Task} созданной задачи
     */
    @LogAround
    public TaskDto createTask(TaskDto task) {
        Task entityToSave = taskMapper.toEntity(task);
        Task savedEntity = taskRepository.save(entityToSave);
        return taskMapper.toDto(savedEntity);
    }

    /**
     * Получить задачу по её идентификатору.
     *
     * @param id ИД задачи
     * @return {@link TaskDto} дто задачи
     */
    @LogAfterReturning
    @LogAfterThrowing
    public TaskDto getTaskById(Long id) {
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
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDtoList(tasks);
    }

    /**
     * Изменить задачу по её идентификатору.
     *
     * @param id ИД задачи
     * @param taskDetails {@link TaskDto} ДТО с данными для изменения задачи
     * @return {@link TaskDto} дто измененной задачи задачи
     */
    @LogAround
    public TaskDto updateTask(Long id, TaskDto taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow();
        taskMapper.updateFromDto(task, taskDetails);
        taskRepository.save(task);
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
