package ru.nms.labs.jmh;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.nms.labs.service.CountingSort;

import java.util.concurrent.TimeUnit;

@Fork(2)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class CountingSortBenchmark {

    private final CountingSort bucketSort = new CountingSort();

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new OptionsBuilder()
                .include(CountingSortBenchmark.class.getSimpleName())
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.JSON)
                .result("counting-benchmark-result/" + System.currentTimeMillis() + ".json")
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void testBucketSort(ExecutionPlan executionPlan, Blackhole blackhole) {
        blackhole.consume(bucketSort.sort(executionPlan.getTestData()));
    }
}
