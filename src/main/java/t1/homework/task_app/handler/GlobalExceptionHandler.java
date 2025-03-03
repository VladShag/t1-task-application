package t1.homework.task_app.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.homework.task_app.exception.NotificationException;
import t1.homework.task_app.exception.TaskBadRequestException;
import t1.homework.task_app.exception.TaskNotFoundException;
import t1.homework.task_app.wrapper.ApiResponse;

/**
 * Кастомый обработчик возникающих ошибок.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleWalletNotFoundException(TaskNotFoundException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(TaskBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleWalletNotFoundException(TaskBadRequestException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(NotificationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleWalletNotFoundException(NotificationException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGlobalException(Exception ex) {
        return ApiResponse.error(ex.getMessage());
    }
}
