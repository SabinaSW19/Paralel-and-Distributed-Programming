import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Operations {
    public Polynomial productSequential(Polynomial p1,Polynomial p2)
    {
        List<Integer> result=new ArrayList<>(p1.degree+ p2.degree+1);
        for (int i = 0; i < p1.degree+ p2.degree+1; i++) {
            result.add(0);
        }
        for(int i=0;i<=p1.degree;i++)
            for(int j=0;j<= p2.degree;j++)
                result.set(i+j,result.get(i+j)+p1.coefficients.get(i)*p2.coefficients.get(j));
        return new Polynomial(result);
    }
    public Polynomial productParallelized(Polynomial p1,Polynomial p2,int numberThreads) throws InterruptedException {
        int sizeOfResultCoefficientList = p1.getDegree() + p2.getDegree() + 1;
        List<Integer> coefficients = IntStream.range(0, sizeOfResultCoefficientList).mapToObj(i -> 0).collect(Collectors.toList());
        Polynomial result = new Polynomial(coefficients);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberThreads);
        int numberElementsPerTask = result.coefficients.size() / numberThreads;
        if (numberElementsPerTask == 0) {
            numberElementsPerTask = 1;
        }

        int end;
        for (int i = 0; i < result.coefficients.size(); i += numberElementsPerTask) {
            end = i + numberElementsPerTask;
            RunThreads task = new RunThreads( p1, p2, result,i, end);
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(50, TimeUnit.SECONDS);

        return result;
    }

    public Polynomial KaratsubaSequential(Polynomial p1,Polynomial p2)
    {
        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return productSequential(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial lowP1 = new Polynomial(p1.getCoefficients().subList(0, len));
        Polynomial highP1 = new Polynomial(p1.getCoefficients().subList(len, p1.coefficients.size()));
        Polynomial lowP2 = new Polynomial(p2.getCoefficients().subList(0, len));
        Polynomial highP2 = new Polynomial(p2.getCoefficients().subList(len, p2.coefficients.size()));

        Polynomial z1 = KaratsubaSequential(lowP1, lowP2);
        Polynomial z2 = KaratsubaSequential(Polynomial.add(lowP1, highP1), Polynomial.add(lowP2, highP2));
        Polynomial z3 = KaratsubaSequential(highP1, highP2);

        //calculate the final result
        Polynomial r1 = Polynomial.addZeros(z3, 2 * len);
        Polynomial r2 = Polynomial.addZeros(Polynomial.subtract(Polynomial.subtract(z2, z3), z1), len);
        Polynomial result = Polynomial.add(Polynomial.add(r1, r2), z1);
        return result;
    }

    public Polynomial KaratsubaParallelized(Polynomial p1, Polynomial p2,int numberThreads,int currentDepth) throws InterruptedException, ExecutionException {
        if (currentDepth > 4) {
            return KaratsubaSequential(p1, p2);
        }

        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return KaratsubaSequential(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial lowP1 = new Polynomial(p1.getCoefficients().subList(0, len));
        Polynomial highP1 = new Polynomial(p1.getCoefficients().subList(len, p1.coefficients.size()));
        Polynomial lowP2 = new Polynomial(p2.getCoefficients().subList(0, len));
        Polynomial highP2 = new Polynomial(p2.getCoefficients().subList(len, p2.coefficients.size()));

        ExecutorService executor = Executors.newFixedThreadPool(numberThreads);
        Future<Polynomial> f1 = executor.submit(() -> KaratsubaParallelized(lowP1, lowP2,numberThreads, currentDepth + 1));
        Future<Polynomial> f2 = executor.submit(() -> KaratsubaParallelized(Polynomial.add(lowP1, highP1), Polynomial
                .add(lowP2, highP2), numberThreads,currentDepth + 1));
        Future<Polynomial> f3 = executor.submit(() -> KaratsubaParallelized(highP1, highP2, numberThreads,currentDepth + 1));

        executor.shutdown();

        Polynomial z1 = f1.get();
        Polynomial z2 = f2.get();
        Polynomial z3 = f3.get();

        executor.awaitTermination(60, TimeUnit.SECONDS);

        //calculate the final result
        Polynomial r1 = Polynomial.addZeros(z3, 2 * len);
        Polynomial r2 = Polynomial.addZeros(Polynomial.subtract(Polynomial.subtract(z2, z3), z1), len);
        Polynomial result = Polynomial.add(Polynomial.add(r1, r2), z1);
        return result;
    }

}
