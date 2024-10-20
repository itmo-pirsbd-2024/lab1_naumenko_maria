package ru.nms.labs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketSort {

    public List<Integer> sort(List<Integer> input, int bucketsAmount) {
        Pair minAndMax = findMinAndMax(input);
        int range = minAndMax.max - minAndMax.min;
        if (range == 0) return input;

        List<List<Integer>> buckets = putToBuckets(input, minAndMax.max, bucketsAmount);
        return flattenBuckets(buckets);
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

    private List<List<Integer>> putToBuckets(List<Integer> input, int max, int bucketsAmount) {
        List<List<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketsAmount; i++) {
            buckets.add(new ArrayList<>());
        }
        for (Integer elem : input) {
            int index = (int) (elem * (double)(bucketsAmount - 1) / (double)max);
            buckets.get(index).add(elem);
        }
        return buckets;
    }

    private List<Integer> flattenBuckets(List<List<Integer>> buckets) {
        List<Integer> result = new ArrayList<>();
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
            result.addAll(bucket);
        }
        return result;
    }

    private static class Pair {
        private final int min;
        private final int max;

        public Pair(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}
