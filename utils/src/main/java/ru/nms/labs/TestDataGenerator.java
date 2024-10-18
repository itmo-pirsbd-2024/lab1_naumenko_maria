package ru.nms.labs;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDataGenerator {
    public static List<Integer> generateRandomList(int size, int randSeed) {
            List<Integer> randomList = new ArrayList<>();
            Random rand = new Random(randSeed);
            for (int i = 0; i < size; i++) {
                int randomValue = Math.abs(rand.nextInt(100000));
                randomList.add(randomValue);
            }
            return randomList;
    }
}
