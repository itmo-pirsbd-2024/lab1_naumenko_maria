package ru.nms.labs;

import org.junit.jupiter.api.Test;
import ru.nms.labs.service.BucketSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BucketSortTest {

    private final BucketSort bucketSort = new BucketSort();

    @Test
    void shouldSortElements() {
        List<Integer> input = TestDataGenerator.generateRandomList(10000000, 0);
        List<Integer> sortedInput = new ArrayList<>(input);
        Collections.sort(sortedInput);
        assertEquals(sortedInput, bucketSort.sort(input, 2000000));
    }
}
