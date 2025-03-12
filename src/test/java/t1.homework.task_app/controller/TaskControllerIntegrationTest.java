package t1.homework.task_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import t1.homework.task_app.dto.TaskRecordDto;
import t1.homework.task_app.repository.TaskRepository;
import t1.homework.task_app.service.TaskService;
import t1.homework.task_app.service.kafka.KafkaProducerService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты контроллера {@link TaskController}.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {
    private static final Long ID_NOT_IN_DB = 99L;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @MockitoBean
    private KafkaProducerService kafkaProducerService;

    private TaskRecordDto taskDto;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskDto = new TaskRecordDto();
        taskDto.setTitle("Test Task");
        taskDto.setStatus("NEW");
    }

    @Test
    void testCreateTask() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(5L))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    @DisplayName("Получение существующей задачи по ID")
    void getTaskById_validId_success() throws Exception {
        TaskRecordDto createdTask = taskService.createTask(taskDto);

        mockMvc.perform(get("/tasks/{id}", createdTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    @DisplayName("Получение задачи по ID которого нет в БД")
    void getTaskById_idNoContainsInDb_throwsNotFoundException() throws Exception {
        taskService.createTask(taskDto);

        mockMvc.perform(get("/tasks/{id}", ID_NOT_IN_DB))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Не найдена задача с id: " + ID_NOT_IN_DB));
    }

    @Test
    @DisplayName("Получение списка всех задач")
    void getAllTasks_success() throws Exception {
        taskService.createTask(taskDto);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    @DisplayName("Обновление задачи при валидной ДТО и ID")
    void updateTask_validDto_success() throws Exception {
        TaskRecordDto createdTask = taskService.createTask(taskDto);
        createdTask.setStatus("IN_PROGRESS");

        mockMvc.perform(put("/tasks/{id}", createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Обновление задачи при валидной ДТО и ID, которого нет в БД")
    void updateTask_idNotContainsInDb_throwsNotFoundException() throws Exception {
        TaskRecordDto createdTask = taskService.createTask(taskDto);
        createdTask.setStatus("IN_PROGRESS");

        mockMvc.perform(put("/tasks/{id}", ID_NOT_IN_DB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdTask)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Не найдено задачи по id: " + ID_NOT_IN_DB));
    }

    @Test
    @DisplayName("Удаление задачи по существующему id")
    void deleteTask_validId_success() throws Exception {
        TaskRecordDto createdTask = taskService.createTask(taskDto);

        mockMvc.perform(delete("/tasks/{id}", createdTask.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", createdTask.getId()))
                .andExpect(status().isNotFound());
    }
}