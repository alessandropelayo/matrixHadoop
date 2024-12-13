package com.matrixmulti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class App {

    public static void main(String[] args) throws IOException {
        /*
        long[][] A = { {5, 2, 3},
                      {8, 3, 32},
                      {13, 5, 12},
                      {2, 8, 3}};

        long[][] B = { {7, 6, 21, 6},
                      {4, 4, 1, 5},
                      {66, 7, 2, 4}};   
        */

        ArrayList<long[][]> matrices = readMatrices("/home/hadoopusr/matrixmulti/matrixHadoop/dataset");

        long startTime = System.currentTimeMillis();
        long[][] result = multiplyMatrix(matrices.get(0), matrices.get(1));

        for (int i = 2; i < matrices.size(); i++) {
            multiplyMatrix(result, matrices.get(i));
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        //printMatrix(result);
        System.out.println("Matrix multiplication execution time: " + duration + " ms.");
    }    

    public static long[][] multiplyMatrix(long[][] A, long[][] B) {
        if (A[0].length != B.length) {
            System.out.println("Incompatible matricies");
        }

        long[][] C = new long[A.length][B[0].length];

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < B.length; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }
    
    public static void printMatrix(long[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static ArrayList<long[][]> readMatrices(String filepath) throws IOException {
        ArrayList<long[][]> matrices = new ArrayList<>();
        ArrayList<long[]> matrix = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!matrix.isEmpty() && line.trim().isEmpty()) {
                    long[][] temp = new long[matrix.size()][];
                    for (int i = 0; i < matrix.size(); i++) {
                        temp[i] = matrix.get(i);
                    }
                    matrices.add(temp);
                    matrix.clear();
                }
                else {
                    String[] rowLine = line.trim().split("\\s+");
                    long[] row = new long[rowLine.length];
                    for (int i = 0; i < rowLine.length; i++) {
                        row[i] = Integer.parseInt(rowLine[i]);
                    }
                    matrix.add(row);
                }
            }
        }

        if (!matrix.isEmpty()) {
            long[][] temp = new long[matrix.size()][];
            for (int i = 0; i < matrix.size(); i++) {
                temp[i] = matrix.get(i);
            }
            matrices.add(temp);
        }

        return matrices;
    }
}
