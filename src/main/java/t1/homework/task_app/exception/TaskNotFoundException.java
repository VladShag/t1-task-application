package t1.homework.task_app.exception;

/**
 * Ошибка, возникающая при не найденной задаче.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
