package t1.homework.task_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.model.Task;
import t1.homework.task_app.service.TaskService;

import java.util.List;

/**
 * Контроллер для работы с задачами.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Создать новую задачу.
     *
     * @param task ДТО для создания задачи
     * @return сущность {@link Task} созданной задачи
     */
    @PostMapping
    public TaskRecordDto createTask(@RequestBody TaskRecordDto task) {
        return taskService.createTask(task);
    }

    /**
     * Получить задачу по её идентификатору.
     *
     * @param id ИД задачи
     * @return {@link TaskRecordDto} дто задачи
     */
    @GetMapping("/{id}")
    public TaskRecordDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    /**
     * Изменить задачу по её идентификатору.
     *
     * @param id ИД задачи
     * @param taskDetails {@link TaskRecordDto} ДТО с данными для изменения задачи
     * @return {@link TaskRecordDto} дто измененной задачи задачи
     */
    @PutMapping("/{id}")
    public TaskRecordDto updateTask(@PathVariable Long id, @RequestBody TaskRecordDto taskDetails) {
        return taskService.updateTaskAndSendNotification(id, taskDetails);
    }

    /**
     * Удалить задачу по её идентификатору.
     *
     * @param id ИД задачи
     */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    /**
     * Получить все доступные задачи.
     *
     * @return список сущностей {@link Task} задач
     */
    @GetMapping
    public List<TaskRecordDto> getAllTasks() {
        return taskService.getAllTasks();
    }
}