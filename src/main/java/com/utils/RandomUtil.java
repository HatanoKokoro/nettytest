package com.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {

    public static long [] getRadmon(int count) {
        long [] rs = new long[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            rs[i] = random.nextLong();
        }
        return rs;
    }

}
