import algorithms.Column;
import algorithms.KthElements;
import algorithms.Row;
import models.Matrix;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    static int n1=2000;
    static int m1=2000;
    static int n2=2000;
    static int m2=2000;
    static int numberThreads=1;
    static Matrix matrix1;
    static Matrix matrix2;
    static Matrix product;
    public static int[][] initializeMatrix(int n,int m)
    {
        int [][] matrix=new int[n][m];
        Random random=new Random();
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
                matrix[i][j]=random.nextInt(10)+1;
        return matrix;
    }
    public static List<Thread> initRowThread()
    {
        int size=n1*m2;
        int numberElementsPerTask=size/numberThreads;
        int i,j;
        int index;
        List<Thread> rowThreads=new ArrayList<>();
        for(index=0;index<numberThreads;index++)
        {
            i=numberElementsPerTask*index/n1;
            j=numberElementsPerTask*index%n1;
            if(index==numberThreads-1)
            {
                numberElementsPerTask+=size-(numberThreads-1)*numberElementsPerTask;
            }
            Row thread=new Row(i,j,numberElementsPerTask,matrix1,matrix2,product);
            rowThreads.add(thread);

        }
        return rowThreads;
    }

    public static List<Thread> initColumnThread()
    {
        int size=n1*m2;
        int numberElementsPerTask=size/numberThreads;
        int i,j;
        int index;
        List<Thread> columnThreads=new ArrayList<>();
        for(index=0;index<numberThreads;index++)
        {
            i=numberElementsPerTask*index%n1;
            j=numberElementsPerTask*index/n1;
            if(index==numberThreads-1)
            {
                numberElementsPerTask+=size-(numberThreads-1)*numberElementsPerTask;
            }
            Column thread=new Column(i,j,numberElementsPerTask,matrix1,matrix2,product);
            columnThreads.add(thread);

        }
        return columnThreads;
    }
    public static List<Thread> initKtElementThread()
    {
        int index;
        List<Thread> kthElementThreads=new ArrayList<>();
        for(index=0;index<numberThreads;index++)
        {

            KthElements thread=new KthElements(index,matrix1,matrix2,product,numberThreads);
            kthElementThreads.add(thread);

        }
        return kthElementThreads;
    }

    public static void main(String[] args) throws InterruptedException {
        int [][]matrix1vec=initializeMatrix(n1,m1);
        int [][]matrix2vec=initializeMatrix(n2,m2);
        matrix1=new Matrix(n1,m1,matrix1vec);
        matrix2=new Matrix(n2,m2,matrix2vec);
//        System.out.println(matrix1.toString());
//        System.out.println(matrix2.toString());
        if(matrix1.n==matrix2.m)
        {
            int[][]productvec=new int[n1][m2];
            product = new Matrix(matrix1.n, matrix2.m,productvec);
            //row algorithm for normal threads
            Instant start = Instant.now();
            List<Thread> rowThreads=initRowThread();
            rowThreads.forEach(Thread::start);
            rowThreads.forEach(t-> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            Instant end = Instant.now();

            System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for row algorithm with normal threads");

            //System.out.println(product);
            //row algorithm for thread pool
            start = Instant.now();
            ExecutorService executorService1=Executors.newFixedThreadPool(numberThreads);
            rowThreads.forEach(executorService1::submit);
            executorService1.shutdown();
            try {
                if (!executorService1.awaitTermination(300, TimeUnit.SECONDS)) {
                    executorService1.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService1.shutdownNow();
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
            end = Instant.now();

            System.out.println("Time elapsed: " + Duration.between(start,end).toMillis()+ " milliseconds for row algorithm with thread pool.");

            //System.out.println(product);

            //column algorithm for normal threads
            start = Instant.now();

            List<Thread> columnThreads=initColumnThread();
            columnThreads.forEach(Thread::start);
            columnThreads.forEach(t-> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            end = Instant.now();

            System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for column algorithm with normal threads.");
            //System.out.println(product);

            //column algorithm for thread pool
            start = Instant.now();
            ExecutorService executorService2=Executors.newFixedThreadPool(numberThreads);
            columnThreads.forEach(executorService2::submit);
            executorService2.shutdown();
            try {
                if (!executorService2.awaitTermination(300, TimeUnit.SECONDS)) {
                    executorService2.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService2.shutdownNow();
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
            end = Instant.now();

            System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for column algorithm with thread pool.");
            //System.out.println(product);

            //kth element algorithm for normal threads
            start = Instant.now();
            List<Thread> kthElementThreads=initKtElementThread();
            kthElementThreads.forEach(Thread::start);
            kthElementThreads.forEach(t-> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            end = Instant.now();

            System.out.println("Time elapsed: " + Duration.between(start,end).toMillis()+ " milliseconds for kth element algorithm with normal threads.");
            //System.out.println(product);

            //kth element algorithm for thread pool
            start = Instant.now();
            ExecutorService executorService3=Executors.newFixedThreadPool(numberThreads);
            kthElementThreads.forEach(executorService3::submit);
            executorService3.shutdown();
            try {
                if (!executorService3.awaitTermination(300, TimeUnit.SECONDS)) {
                    executorService3.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService3.shutdownNow();
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
            end = Instant.now();

            System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for kth element algorithm with thread pool.");
            //System.out.println(product);
        }
        else
            System.out.println("We can't compute de product of these 2 matrices!!");

    }
}
