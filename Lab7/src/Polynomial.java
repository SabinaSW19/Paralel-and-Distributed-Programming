import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Polynomial implements Serializable {
    List<Integer> coefficients;
    int degree;

    public Polynomial(int degree) {
        this.degree=degree;
        this.coefficients=new ArrayList<>(degree+1);
        generateCoefficients();
    }

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
        this.degree = coefficients.size() - 1;
    }


    private void generateCoefficients() {
        Random r = new Random();
        for (int i = 0; i < degree; i++) {
            coefficients.add(r.nextInt(10));
        }
        coefficients.add(r.nextInt(10) + 1);
    }

    public List<Integer> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public static Polynomial addZeros(Polynomial p, int index) {
        List<Integer> coefficients = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            coefficients.add(0);
        }
        coefficients.addAll(p.getCoefficients());
        return new Polynomial(coefficients);
    }
    public static Polynomial add(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        //Add the 2 polynomials
        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getCoefficients().get(i) + p2.getCoefficients().get(i));
        }

        addRemainingCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        return new Polynomial(coefficients);
    }

    private static void addRemainingCoefficients(Polynomial p1, Polynomial p2, int minDegree, int maxDegree, List<Integer> coefficients) {
        if (minDegree != maxDegree) {
            if (maxDegree == p1.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p1.getCoefficients().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p2.getCoefficients().get(i));
                }
            }
        }
    }

    public static Polynomial subtract(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        //Subtract the 2 polynomials
        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getCoefficients().get(i) - p2.getCoefficients().get(i));
        }

        addRemainingCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        //remove coefficients starting from biggest power if coefficient is 0

        int i = coefficients.size() - 1;
        while (coefficients.get(i) == 0 && i > 0) {
            coefficients.remove(i);
            i--;
        }

        return new Polynomial(coefficients);
    }


    @Override
    public String toString() {
        StringBuilder polynomial=new StringBuilder();
        for(int i=0;i<=this.degree;i++)
        {
            if(i==0)
                polynomial.append(this.coefficients.get(i)).append("+");
            else
            if(this.coefficients.get(i)!=0) {
                if(i==1)
                    polynomial.append(this.coefficients.get(i)).append("x").append("+");
                else
                if (this.coefficients.get(i) != 1)
                    polynomial.append(this.coefficients.get(i)).append("x^").append(i).append("+");
                else
                    polynomial.append("x^").append(i).append("+");
            }

        }
        polynomial.deleteCharAt(polynomial.length()-1);
        return polynomial.toString();
    }
    public void setCoefficient(int pos, int val) {
        this.coefficients.set(pos, val);
    }
    public static Polynomial buildEmptyPolynomial(int degree){

        List<Integer> empty= new ArrayList<>();
        for(int i=0;i<degree;i++)
            empty.add(0);
        return new Polynomial(empty);
    }
}
