package org.saumya.lld.taskScheduler.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.taskScheduler.enums.TaskStatus;
import org.saumya.lld.taskScheduler.enums.TaskType;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Task {
    String id;
    Runnable command;
    TaskType taskType;
    long intervalMillis;
    int maxRetries;


    AtomicReference<TaskStatus> status = new AtomicReference<>(TaskStatus.PENDING);

    AtomicInteger attempts = new AtomicInteger(0);

    public boolean transitionTo(TaskStatus expected, TaskStatus next) {
        return status.compareAndSet(expected, next);
    }

    public TaskStatus getStatus() {
        return status.get();
    }

    public void forceStatus(TaskStatus taskStatus) {
        status.set(taskStatus);
    }

    public int getAttempts() {
        return attempts.get();
    }

    public int incrementAndGetAttempts() {
        return attempts.incrementAndGet();
    }
}
