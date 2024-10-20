package ru.nms.labs.jmh;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import ru.nms.labs.TestDataGenerator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({"1"})
    private int seed;

    @Param({"50000000"})
    private int size;

    @Getter
    @Param({"10000000"})
    private int bucketsAmount;

    @Getter
    private final List<Integer> testData = new ArrayList<>();

    @Setup(Level.Trial)
    public void setup() {
        testData.clear();
        testData.addAll(TestDataGenerator.generateRandomList(size, seed));
    }
}
