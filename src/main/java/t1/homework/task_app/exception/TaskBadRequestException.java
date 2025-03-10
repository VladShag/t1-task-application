package t1.homework.task_app.exception;

/**
 * Ошибка, возникающая при некорректных запросах.
 */
public class TaskBadRequestException extends RuntimeException {
    public TaskBadRequestException(String message) {
        super(message);
    }
}
