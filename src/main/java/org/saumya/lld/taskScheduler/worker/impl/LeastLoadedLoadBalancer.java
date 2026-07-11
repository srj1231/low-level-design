package org.saumya.lld.taskScheduler.worker.impl;

import org.saumya.lld.taskScheduler.worker.LoadBalancer;
import org.saumya.lld.taskScheduler.worker.WorkerNode;

import java.util.Comparator;
import java.util.List;

public class LeastLoadedLoadBalancer implements LoadBalancer {
    @Override
    public WorkerNode selectWorker(List<WorkerNode> workers) {
        return workers.stream()
                .min(Comparator.comparingInt(WorkerNode::getLoad))
                .orElseThrow(() -> new IllegalStateException("No available workers"));
    }
}
