package t1.homework.task_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import t1.homework.task_app.model.Task;

/**
 * Репозиторий для работы с таблицей tasks.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}