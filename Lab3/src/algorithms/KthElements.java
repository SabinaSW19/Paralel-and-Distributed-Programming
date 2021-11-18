package algorithms;

import models.Matrix;

public class KthElements extends Thread{

    int k;
    int start;
    Matrix first;
    Matrix second;
    Matrix product;

    public KthElements(int start,Matrix first,Matrix second,Matrix product,int k){

        this.first=first;
        this.second=second;
        this.product=product;
        this.start=start;
        this.k=k;
    }

    public int generateElement(int i,int j)
    {
        int element = 0;
        for (int k=0;k<first.m;k++){
            element+=first.matrix[i][k] * second.matrix[k][j];
        }
        return element;
    }


    @Override
    public void run(){
        int i = start/ product.n, j = start% product.n;
        while (i < this.product.m && j<this.product.n) {
            int element=generateElement(i,j);
            product.matrix[i][j]=element;
            i=i+(j+k)/ product.n;
            j=(j+k)% product.n;

        }

    }
}
