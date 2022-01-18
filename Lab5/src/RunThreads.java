import java.util.List;

public class RunThreads implements Runnable {
    Polynomial p1;
    Polynomial p2;
    Polynomial result;
    int start;
    int end;

    public RunThreads(Polynomial p1, Polynomial p2, Polynomial result, int start, int end) {
        this.p1 = p1;
        this.p2 = p2;
        this.result = result;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            if (i > result.coefficients.size()) {
                return;
            }
            for (int j = 0; j <= i; j++) {
                if (j < p1.coefficients.size() && (i - j) < p2.coefficients.size()) {
                    int value = p1.getCoefficients().get(j) * p2.getCoefficients().get(i- j);
                    result.coefficients.set(i, result.coefficients.get(i) + value);
                }
            }

        }
    }
}
