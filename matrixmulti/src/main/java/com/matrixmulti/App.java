package com.matrixmulti;

public final class App {

    public static void main(String[] args) {
        int[][] A = { {5, 2, 3},
                      {8, 3, 32},
                      {13, 5, 12},
                      {2, 8, 3}};

        int[][] B = { {7, 6, 21, 6},
                      {4, 4, 1, 5},
                      {66, 7, 2, 4}};   
                      
        multiplyMatrix(A, B);
    }    

    static void multiplyMatrix(int[][] A, int[][] B) {
        if (A[0].length != B.length) {
            System.out.println("Incompatible matricies");
        }

        int C[][] = new int[A.length][B[0].length];

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < B.length; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C[0].length; j++) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }
    }
}
