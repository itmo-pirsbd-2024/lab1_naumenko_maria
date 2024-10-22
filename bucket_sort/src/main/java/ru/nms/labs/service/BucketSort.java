package ru.nms.labs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketSort {

    public List<Integer> sort(List<Integer> input) {
        Pair minAndMax = findMinAndMax(input);
        int range = minAndMax.max - minAndMax.min;
        if (range == 0) return input;

        List<List<Integer>> buckets = putToBuckets(input, minAndMax.max);
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

    private List<List<Integer>> putToBuckets(List<Integer> input, int max) {
        List<List<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i < input.size() + 1; i++) {
            buckets.add(new ArrayList<>());
        }
        for (Integer elem : input) {
            int index = (int) (elem * (double)input.size() / (double)max);
//            System.out.println("!!!");
//            System.out.println(elem + " " + input.size() + " " + max);
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
