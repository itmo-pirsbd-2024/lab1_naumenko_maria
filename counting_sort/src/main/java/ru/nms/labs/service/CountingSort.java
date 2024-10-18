package ru.nms.labs.service;

import java.util.ArrayList;
import java.util.List;

public class CountingSort {

    public List<Integer> sort(List<Integer> input) {
        List<Integer> countedInput = countElements(input);
        return formResultList(countedInput);
    }

    private List<Integer> countElements(List<Integer> input) {
        List<Integer> countedInput = new ArrayList<>();
        for (Integer elem: input) {
            if (elem >= countedInput.size()) growListToSize(countedInput, elem);
            countedInput.set(elem, countedInput.get(elem) + 1);
        }
        return countedInput;
    }

    private List<Integer> formResultList(List<Integer> countedInput) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < countedInput.size(); i++) {
            for (int k = 0; k < countedInput.get(i); k++) {
                result.add(i);
            }
        }
        return result;
    }

    private void growListToSize(List<Integer> list, int size) {
        int diff = size - list.size() + 1;
        for (int i = 0; i < diff; i++) {
            list.add(0);
        }
    }
}
