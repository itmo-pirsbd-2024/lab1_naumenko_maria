package ru.nms.labs.service;

import java.util.Arrays;

public class CountingSort {

    public int[] sort(int[] input) {
        int maxElem = max(input);
        int[] countedInput = countElements(input, maxElem);
        return formResultList(countedInput, input);
    }

    private int[] countElements(int[] input, int maxElem) {
        int[] countedInput = new int[maxElem + 1];
        for (int elem : input) {
            countedInput[elem] = countedInput[elem] + 1;
        }
        return countedInput;
    }

    private int[] formResultList(int[] countedInput, int[] input) {
        int index = 0;
        for (int i = 0; i < countedInput.length; i++) {
            int amountOfElements = countedInput[i];
            Arrays.fill(input, index, index + amountOfElements, i);
            index += amountOfElements;
        }
        return input;
    }

    private int max(int[] input) {
        return Arrays.stream(input).parallel().max().orElse(0);
    }
}
