package org.saumya.lld.taskScheduler.task;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.taskScheduler.enums.TaskStatus;
import org.saumya.lld.taskScheduler.enums.TaskType;
import org.saumya.lld.taskScheduler.repository.TaskRepository;
import org.saumya.lld.taskScheduler.worker.WorkerManager;
import org.saumya.lld.taskScheduler.worker.WorkerNode;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadLocalRandom;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskScheduler {
    final DelayQueue<ScheduledJob> queue = new DelayQueue<>();
    final TaskRepository taskRepository;
    final WorkerManager workerManager;
    final Thread dispatcherThread;
    volatile boolean running = true;

    public TaskScheduler(WorkerManager workerManager, TaskRepository taskRepository) {
        this.workerManager = workerManager;
        this.taskRepository = taskRepository;
        this.dispatcherThread = new Thread(this::dispatchLoop, "scheduler-dispatcher");
        this.dispatcherThread.start();
    }

    public String scheduleOneTime(Runnable command, long delayInMills) {
        return scheduleOneTime(command, delayInMills, 0);
    }

    public String scheduleOneTime(Runnable command, long delayInMills, int maxRetries) {
        return schedule(command, delayInMills, maxRetries, TaskType.ONE_TIME, 0, maxRetries);
    }

    public String scheduleRecurring(Runnable command, long delayInMills, long intervalInMills) {
        return scheduleRecurring(command, delayInMills, intervalInMills, 0);
    }

    public String scheduleRecurring(Runnable command, long delayInMillis, long intervalInMillis, int maxRetries) {
        return schedule(command, delayInMillis, maxRetries, TaskType.RECURRING, intervalInMillis, maxRetries);
    }

    private String schedule(Runnable command, long delayInMills, int maxRetries, TaskType taskType, long intervalInMills, int maxReries) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, command, taskType, intervalInMills, maxReries);
        task.forceStatus(TaskStatus.SCHEDULED);
        taskRepository.save(task);
        queue.put(new ScheduledJob(System.currentTimeMillis() + delayInMills, task));
        return id;
    }

    public boolean cancel(String taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if(task == null) return false;
        boolean cancelled = task.transitionTo(TaskStatus.SCHEDULED, TaskStatus.CANCELLED);
        if(cancelled) taskRepository.save(task);
        return cancelled;
    }

    public TaskStatus getStatus(String taskId) {
        return taskRepository.findById(taskId)
                .map(Task::getStatus)
                .orElseThrow(() -> new NoSuchElementException("Unknown task: " + taskId));
    }

    private void dispatchLoop() {
        while(running) {
            ScheduledJob job;
            try {
                job = queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Task task = job.getTask();
            if(!task.transitionTo(TaskStatus.SCHEDULED, TaskStatus.CANCELLED)) {
                continue;
            }
            taskRepository.save(task);

            try{
                WorkerNode workerNode = workerManager.pickWorker();
                workerNode.submit(task, this::onTaskFinish);
            } catch (Exception e) {
                task.forceStatus(TaskStatus.SCHEDULED);
                taskRepository.save(task);
                queue.put(new ScheduledJob(System.currentTimeMillis() + 500, task));
            }
        }
    }

    private void onTaskFinish(Task task) {
        taskRepository.save(task);

        if(task.getStatus().equals(TaskStatus.FAILED) && task.getAttempts() < task.getMaxRetries()) {
            int attempt = task.incrementAndGetAttempts();
            task.forceStatus(TaskStatus.SCHEDULED);
            taskRepository.save(task);
            queue.put(new ScheduledJob(System.currentTimeMillis() + backoffMillis(attempt), task));
            return;
        }
        if(task.getTaskType() == TaskType.RECURRING && task.getStatus() != TaskStatus.CANCELLED) {
            task.forceStatus(TaskStatus.SCHEDULED);
            taskRepository.save(task);
            queue.put(new ScheduledJob(System.currentTimeMillis() + task.getIntervalMillis(), task));
        }
    }

    private long backoffMillis(int attempt) {
        long base = (long) Math.pow(2, attempt) * 1000;
        long jitter = ThreadLocalRandom.current().nextLong(0, 250);
        return base + jitter;
    }

    public void shutdown() {
        running = false;
        dispatcherThread.interrupt();
    }
}
