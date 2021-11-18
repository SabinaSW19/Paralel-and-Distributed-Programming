package models;

public class Matrix {
    public int n;
    public int m;
    public int [][]matrix;
    public Matrix(int n,int m, int[][] matrix)
    {
        this.n=n;
        this.m=m;
        this.matrix=matrix;
    }
    public String toString()
    {
        String s="";
        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++)
                s += matrix[i][j] + " ";
            s+="\n";
        }
        return s;
    }
}
