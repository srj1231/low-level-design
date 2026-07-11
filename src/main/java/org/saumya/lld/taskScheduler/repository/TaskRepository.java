package org.saumya.lld.taskScheduler.repository;

import org.saumya.lld.taskScheduler.task.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void save(Task task);

    Optional<Task> findById(String taskId);

    List<Task> findAll();
}
