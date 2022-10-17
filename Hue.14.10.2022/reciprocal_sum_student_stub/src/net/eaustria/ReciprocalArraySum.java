
package net.eaustria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
public final class ReciprocalArraySum {

    public static void main(String[] args) {
        double[] arr = {1.0 , 2.0 , 3.0 , 4.0};
        ReciprocalArraySumTask rt = new ReciprocalArraySumTask(0,arr.length,arr);
        rt.compute();
    }
    private ReciprocalArraySum() {
    }
    protected static double seqArraySum(final double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += (1/input[i]);
        }
        return sum;
    }
    private static class ReciprocalArraySumTask extends RecursiveAction {
        private final int startIndexInclusive;
        private final int endIndexExclusive;
        private final double[] input;
        private double value;
        
        private static int SEQUENTIAL_THRESHOLD = 50000;

        ReciprocalArraySumTask(final int setStartIndexInclusive,
                final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = shortenArray(setInput);
        }
        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        private double[] shortenArray(double[] input)
        {
            return Arrays.stream(input).boxed().toList().subList(startIndexInclusive, endIndexExclusive).stream().mapToDouble(n -> n).toArray();
        }

        @Override
        protected void compute() {
           if(input.length < SEQUENTIAL_THRESHOLD)
           {
               double result = seqArraySum(input);
               setValue(result+getValue());
               System.out.println(result);
           }
           else {
               ReciprocalArraySumTask task1 = new ReciprocalArraySumTask(0, input.length/2,input);
               ReciprocalArraySumTask task2 = new ReciprocalArraySumTask((input.length/2)+1, input.length,input);
               System.out.println("new task");
               invokeAll(task1,task2);
           }
            
        }
    }

    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
       double sum = 0;
       ForkJoinPool fj = new ForkJoinPool(numTasks);
       ReciprocalArraySumTask task =  new ReciprocalArraySumTask(0, input.length,input);
       fj.invoke(task);
       sum = task.getValue();
       return sum;
    }
}

