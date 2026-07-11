package org.saumya.lld.taskScheduler.worker;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.taskScheduler.enums.TaskStatus;
import org.saumya.lld.taskScheduler.task.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WorkerNode {

    @Getter
    String id;

    ExecutorService executor;
    AtomicInteger activeTaskCount = new AtomicInteger(0);

    public WorkerNode(String id, int maxThreads) {
        this.id = id;
        this.executor = Executors.newFixedThreadPool(maxThreads);
    }

    public void submit(Task task, Consumer<Task> onFinish) {
        activeTaskCount.incrementAndGet();
        executor.submit(() -> {
            try {
                task.getCommand().run();
                task.forceStatus(TaskStatus.COMPLETED);
            }
            catch (Exception e) {
                task.forceStatus(TaskStatus.FAILED);
            }
            finally {
                activeTaskCount.decrementAndGet();
                onFinish.accept(task);
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }

    public int getLoad() {
        return activeTaskCount.get();
    }
}
