package org.saumya.lld.taskScheduler.worker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.CopyOnWriteArrayList;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class WorkerManager {
    LoadBalancer loadBalancer;
    CopyOnWriteArrayList<WorkerNode> workers = new CopyOnWriteArrayList<>();

    public void addWorker(WorkerNode worker) {
        workers.add(worker);
    }

    public void removeWorker(WorkerNode worker) {
        workers.removeIf(w -> {
            boolean match = w.getId().equals(worker.getId());
            if(match) w.shutdown();
            return match;
        });
    }

    public WorkerNode pickWorker() {
        return loadBalancer.selectWorker(workers);
    }
}
