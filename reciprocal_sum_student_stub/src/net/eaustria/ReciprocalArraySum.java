
package net.eaustria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
public final class ReciprocalArraySum {
    private ReciprocalArraySum() {
    }
    protected static double seqArraySum(final double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += input[i];
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
            this.input = setInput;
        }
        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        @Override
        protected void compute() {
           if(input.length < value)
           {
               double result = seqArraySum(input);
               setValue(result);
               System.out.println(result);
           }
           else {
               ReciprocalArraySumTask task1 = new ReciprocalArraySumTask(0, input.length/2,input);
               ReciprocalArraySumTask task2 = new ReciprocalArraySumTask((input.length/2)+1, input.length,input);
                invokeAll(task1,task2);
           }
            
        }
    }
  

    /**
     * TODO: Extend the work you did to implement parArraySum to use a set
     * number of tasks to compute the reciprocal array sum. 
     *
     * @param input Input array
     * @param numTasks The number of tasks to create
     * @return The sum of the reciprocals of the array input
     */
    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
       double sum = 0;
       ForkJoinPool fj = new ForkJoinPool(numTasks);
       ReciprocalArraySumTask task =  new ReciprocalArraySumTask(0, input.length,input);
       fj.invoke(task);
       sum = task.getValue();
       return sum;
    }
}

