package ru.nms.labs.service;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

public class CountingSort {

    public int[] sort(int[] input) {
        int maxElem = max(input);
        AtomicIntegerArray countedInput = countElements(input, maxElem);
        return formResultList(countedInput, input);
    }

    private AtomicIntegerArray countElements(int[] input, int maxElem) {
        AtomicIntegerArray countedInput = new AtomicIntegerArray(maxElem + 1);

        IntStream.of(input).parallel().forEach(countedInput::incrementAndGet);
        return countedInput;
    }

    private int[] formResultList(AtomicIntegerArray countedInput, int[] input) {
        int index = 0;
        for (int i = 0; i < countedInput.length(); i++) {
            int amountOfElements = countedInput.get(i);
            Arrays.fill(input, index, index + amountOfElements, i);
            index += amountOfElements;
        }
        return input;
    }

    private int max(int[] input) {
        return Arrays.stream(input).parallel().max().orElse(0);
    }
}
