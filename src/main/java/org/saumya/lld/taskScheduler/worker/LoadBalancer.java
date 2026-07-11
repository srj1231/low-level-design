package org.saumya.lld.taskScheduler.worker;

import java.util.List;

public interface LoadBalancer {

    WorkerNode selectWorker(List<WorkerNode> workers);
}
