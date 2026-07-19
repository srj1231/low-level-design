package org.saumya.lld.taskScheduler;

import org.saumya.lld.taskScheduler.repository.InMemoryTaskRepo;
import org.saumya.lld.taskScheduler.task.TaskScheduler;
import org.saumya.lld.taskScheduler.worker.WorkerManager;
import org.saumya.lld.taskScheduler.worker.WorkerNode;
import org.saumya.lld.taskScheduler.worker.impl.LeastLoadedLoadBalancer;

public class TaskSchedulerRunner {
    public static void main(String[] args) throws InterruptedException {
        WorkerManager workerManager = new WorkerManager(new LeastLoadedLoadBalancer());
        workerManager.addWorker(new WorkerNode("worker-1", 2));
        workerManager.addWorker(new WorkerNode("worker-2", 2));

        TaskScheduler taskScheduler = new TaskScheduler(workerManager, new InMemoryTaskRepo());

        String t1 = taskScheduler.scheduleOneTime(() -> {
            System.out.println("One-time task, Task 1 executed");
        }, 1000);

        String t2 = taskScheduler.scheduleRecurring(() -> {
            System.out.println("Recurring task, Task 2 executed");
        }, 500, 2000, 3);

        Thread.sleep(2000);
        System.out.println("t1 status: " + taskScheduler.getStatus(t1));
        System.out.println("t2 status: " + taskScheduler.getStatus(t2));

        Thread.sleep(1000);
        taskScheduler.cancel(t2);

        System.out.println("t1 status: " + taskScheduler.getStatus(t1));
        System.out.println("t2 status: " + taskScheduler.getStatus(t2));
        taskScheduler.shutdown();
        System.out.println("Shutdown.");
    }
}