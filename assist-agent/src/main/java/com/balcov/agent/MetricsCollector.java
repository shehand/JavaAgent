package com.balcov.agent;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetricsCollector {
    private static final
    ConcurrentHashMap<String, Entry> entries = new ConcurrentHashMap<>();

    private static final double alpha = 0.015;

    /**
     * Report method call metrics.
     */
    public static void report(final String methodName,
                              final long duration) {

        entries.compute(methodName,
                (final String key,
                 final Entry curr) -> {
                    if (curr == null) {
                        return new Entry(1L, duration);
                    }

                    final long newAvgDuration = Math.round(
                            curr.getAvgDuration() * (1 - alpha) + duration * alpha);
                    return new Entry(
                            curr.getCallCounts() + 1, newAvgDuration);
                });
    }

    public static class Entry {
        private final long callCounts;
        private final long avgDuration;

        private Entry(final long callCounts, final long avgDuration) {
            this.callCounts = callCounts;
            this.avgDuration = avgDuration;
        }

        public long getCallCounts() {
            return callCounts;
        }

        public long getAvgDuration() {
            return avgDuration;
        }
    }

    public static Map<String, Entry> getEntries() {
        return Collections.unmodifiableMap(entries);
    }
}
