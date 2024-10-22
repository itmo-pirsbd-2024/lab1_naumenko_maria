package ru.nms.labs;

import org.junit.jupiter.api.Test;
import ru.nms.labs.service.CountingSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountingSortTest {

    private final CountingSort countingSort = new CountingSort();

    @Test
    void shouldSortElements() {
        List<Integer> input = TestDataGenerator.generateRandomList(1000, 0);
        List<Integer> sortedInput = new ArrayList<>(input);
        Collections.sort(sortedInput);
        assertEquals(sortedInput, countingSort.sort(input));
    }
}
