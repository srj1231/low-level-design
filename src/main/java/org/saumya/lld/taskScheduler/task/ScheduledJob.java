package org.saumya.lld.taskScheduler.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ScheduledJob implements Delayed {

    long executionMillis;

    @Getter
    Task task;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(executionMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(executionMillis, ((ScheduledJob) o).executionMillis);
    }

}
