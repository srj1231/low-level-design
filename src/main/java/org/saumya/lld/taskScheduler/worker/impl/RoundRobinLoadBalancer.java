package org.saumya.lld.taskScheduler.worker.impl;

import org.saumya.lld.taskScheduler.worker.LoadBalancer;
import org.saumya.lld.taskScheduler.worker.WorkerNode;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer implements LoadBalancer {
    AtomicInteger counter = new AtomicInteger(0);
    @Override
    public WorkerNode selectWorker(List<WorkerNode> workers) {
        if(workers.isEmpty()) {
            throw new IllegalArgumentException("No available workers");
        }

        int index = Math.floorMod(counter.getAndIncrement(), workers.size());
        return workers.get(index);
    }
}
