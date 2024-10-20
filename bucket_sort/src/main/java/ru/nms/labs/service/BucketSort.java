package ru.nms.labs.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BucketSort {

    public List<Integer> sort(List<Integer> input, int bucketsAmount) {
        Pair minAndMax = findMinAndMax(input);
        int range = minAndMax.max - minAndMax.min;
        if (range == 0) return input;

        return input.parallelStream()
                .collect(Collectors.groupingBy(elem -> (int) (elem * (double) (bucketsAmount - 1) / (double) minAndMax.max)))
                .entrySet()
                .parallelStream()
                .peek((entry) -> Collections.sort(entry.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .flatMap(entry -> entry.getValue().stream())
                .toList();
    }

    private Pair findMinAndMax(List<Integer> input) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (Integer elem : input) {
            if (elem > max) max = elem;
            if (elem < min) min = elem;
        }
        return new Pair(min, max);
    }

    private record Pair(int min, int max) {
    }
}
