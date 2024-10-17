package ru.nms.labs.service;

import java.util.ArrayList;
import java.util.List;

public class CountingSort {

    public List<Integer> sort(List<Integer> input) {
        Integer maxElem = max(input);
        int[] countedInput = countElements(input, maxElem);
        return formResultList(countedInput);
    }

    private Integer max(List<Integer> input) {
        Integer max = Integer.MIN_VALUE;
        for (Integer elem: input) {
            if (elem >= max) max = elem;
        }
        return max;
    }

    private int[] countElements(List<Integer> input, Integer maxElem) {
        int[] countedInput = new int[maxElem + 1];
        for (Integer elem: input) {
            countedInput[elem] = countedInput[elem] + 1;
        }
        return countedInput;
    }

    private List<Integer> formResultList(int[] countedInput) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < countedInput.length; i++) {
            for (int k = 0; k < countedInput[i]; k++) {
                result.add(i);
            }
        }
        return result;
    }
}
