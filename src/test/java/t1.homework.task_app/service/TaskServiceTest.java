package t1.homework.task_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.dto.TaskStatusChangeDto;
import t1.homework.task_app.exception.TaskBadRequestException;
import t1.homework.task_app.exception.TaskNotFoundException;
import t1.homework.task_app.mapper.TaskMapper;
import t1.homework.task_app.model.Task;
import t1.homework.task_app.repository.TaskRepository;
import t1.homework.task_app.service.kafka.KafkaProducerService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Модульные тесты класса {@link TaskService}.
 */
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    private static final String STATUS_NEW = "NEW";
    private static final String STATUS_IN_PROGRESS = "IN PROGRESS";

    private static final Long DEFAULT_ID = 1L;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskRecordDto taskDto;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(DEFAULT_ID);
        task.setStatus(STATUS_NEW);

        taskDto = new TaskRecordDto();
        taskDto.setId(DEFAULT_ID);
        taskDto.setStatus(STATUS_NEW);
    }

    @Test
    @DisplayName("Проверка создания задачи при передачи корректной ДТО")
    void createTask_validDto_success() {
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskRecordDto result = taskService.createTask(taskDto);

        assertNotNull(result);
        assertEquals(taskDto, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Проверка получения задачи по ID при передаче корректного ID")
    void getTaskById_validId_success() {
        when(taskRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskRecordDto result = taskService.getTaskById(DEFAULT_ID);

        assertNotNull(result);
        assertEquals(taskDto, result);
        verify(taskRepository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    @DisplayName("Проверка получения задачи при условии, что ID не найден в базе")
    void getTaskById_idNotFound_throwTaskNotFound() {
        when(taskRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(DEFAULT_ID));
        verify(taskRepository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    @DisplayName("Успешное получение списка всех задач")
    void getAllTasks_success() {
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toDtoList(tasks)).thenReturn(List.of(taskDto));

        List<TaskRecordDto> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskDto, result.get(0));
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Успешное обновление задачи")
    void updateTaskAndSendNotification_validRequest_success() {
        TaskRecordDto updatedTaskDto = new TaskRecordDto();
        updatedTaskDto.setId(1L);
        updatedTaskDto.setStatus(STATUS_IN_PROGRESS);
        TaskStatusChangeDto taskStatusChangeDto = new TaskStatusChangeDto();
        taskStatusChangeDto.setTaskId(1L);
        taskStatusChangeDto.setNewStatus(STATUS_IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(updatedTaskDto);
        when(taskMapper.toTaskStatusChangeDto(any(Task.class), anyString())).thenReturn(taskStatusChangeDto);


        TaskRecordDto result = taskService.updateTaskAndSendNotification(DEFAULT_ID, updatedTaskDto);

        assertNotNull(result);
        assertEquals(updatedTaskDto, result);
        verify(taskRepository, times(1)).save(task);
        verify(kafkaProducerService, times(1)).sendTaskStatusChange(taskStatusChangeDto);
    }

    @Test
    @DisplayName("Обновление задачи при условии, что переданный ID отсутствует в базе")
    void updateTaskAndSendNotification_taskNotContainsInDB_throwTaskNotFound() {
        TaskRecordDto updatedTaskDto = new TaskRecordDto();
        updatedTaskDto.setId(DEFAULT_ID);
        updatedTaskDto.setStatus(STATUS_IN_PROGRESS);

        when(taskRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskAndSendNotification(DEFAULT_ID, updatedTaskDto));
        verify(taskRepository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    @DisplayName("Обновление задачи при условии, что передана DTO = null")
    void updateTaskAndSendNotification_nullTaskDetails_throwsTaskBadRequestException() {
        assertThrows(TaskBadRequestException.class, () -> taskService.updateTaskAndSendNotification(DEFAULT_ID, null));
    }

    @Test
    @DisplayName("Успешное удаление задачи из БД")
    void deleteTask_success() {
        taskService.deleteTask(DEFAULT_ID);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}