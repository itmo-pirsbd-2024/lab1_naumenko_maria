package ru.nms.labs.jmh;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.nms.labs.service.BucketSort;

import java.util.concurrent.TimeUnit;

@Fork(2)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class BucketSortBenchmark {

    private final BucketSort bucketSort = new BucketSort();

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new OptionsBuilder()
                .include(BucketSortBenchmark.class.getSimpleName())
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.JSON)
                .result("bucket-benchmark-result/" + System.currentTimeMillis() + ".json")
                .addProfiler(StackProfiler.class)
                .jvmArgsAppend("-Djmh.stack.period=1")
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
//        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void testBucketSort(ExecutionPlan executionPlan, Blackhole blackhole) {
        blackhole.consume(bucketSort.sort(executionPlan.getTestData()));
    }
}
