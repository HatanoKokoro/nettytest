package com.forkjointest;

import com.alibaba.fastjson.JSON;
import com.utils.RandomUtil;

import java.util.Arrays;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MineForkJoinTask extends RecursiveTask<Long>{

    private final static int count = 100;

    private int end;
    private int start;
    private long [] array;

    public MineForkJoinTask( int start,int end, long [] array) {
        this.start = start;
        this.end = end;
        this.array = array;
    }

    @Override
    protected Long compute() {
        if (end - start < count) {
            long sum = 0L;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            MineForkJoinTask leftTask = new MineForkJoinTask(start, mid, this.array);
            MineForkJoinTask rightTask = new MineForkJoinTask( mid, end, this.array);
            invokeAll(leftTask, rightTask);
            long leftrs = leftTask.join();
            long rightrs = rightTask.join();
            long rs = leftrs + rightrs;
            return  rs;
        }
    }

    public static void main(String[] args) {
        long [] radmon = RandomUtil.getRadmon(100000000);
        long ftime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        MineForkJoinTask task = new MineForkJoinTask(0, radmon.length, radmon);
        Long invoke = pool.invoke(task);
        System.out.println("fork time:"+(System.currentTimeMillis() - ftime)+" ms sum:"+invoke);
        long sum = 0;
        long ntime = System.currentTimeMillis();
        for (long i : radmon) {
            sum += i;
        }
        System.out.println("ori time:"+(System.currentTimeMillis() - ntime)+" ms sum:"+sum);

/*        long etime = System.currentTimeMillis();
        long collect = Arrays.stream(radmon).parallel().collect(Collectors.summingLong(i -> i));
        System.out.println("stram time:"+(System.currentTimeMillis() - etime)+" ms sum:"+collect);*/


        long stime = System.currentTimeMillis();
        long scollect = Arrays.stream(radmon).parallel().reduce(0L, Long :: sum);
        System.out.println("stram time:"+(System.currentTimeMillis() - stime)+" ms sum:"+scollect);
    }
}
