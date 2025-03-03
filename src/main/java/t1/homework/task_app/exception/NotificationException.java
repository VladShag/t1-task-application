package t1.homework.task_app.exception;

/**
 * Ошибка, возникающая при отправке уведомления.
 */
public class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message);
    }
}
