package algorithms;

import models.Matrix;

public class Row extends Thread{
    int iStart;
    int jStart;
    int size;
    Matrix first;
    Matrix second;
    Matrix product;

    public Row(int iStart,int jStart,int size,Matrix first,Matrix second,Matrix product){
        this.iStart=iStart;
        this.jStart=jStart;
        this.size=size;
        this.first=first;
        this.second=second;
        this.product=product;
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
        int i = this.iStart, j = this.jStart;
        int size = this.size;
        while (size > 0 && i < this.product.m) {
            int element=generateElement(i,j);
            product.matrix[i][j]=element;
            j++;
            size--;
            if (j == product.n) {
                j = 0;
                i++;
            }
        }

    }
}
