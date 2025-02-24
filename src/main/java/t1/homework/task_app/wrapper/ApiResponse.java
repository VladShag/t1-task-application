package t1.homework.task_app.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Обертка для ответов методов контроллера.
 */
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, "Success", true);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message, true);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(null, message, false);
    }
}
