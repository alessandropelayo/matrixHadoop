package com.matrixmulti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class App {

    public static void main(String[] args) throws IOException {
        /*
        int[][] A = { {5, 2, 3},
                      {8, 3, 32},
                      {13, 5, 12},
                      {2, 8, 3}};

        int[][] B = { {7, 6, 21, 6},
                      {4, 4, 1, 5},
                      {66, 7, 2, 4}};   
        */

        List<int[][]> matrices = readMatrices("100.in");

        long startTime = System.currentTimeMillis();
        int[][] result = multiplyMatrix(matrices.get(0), matrices.get(1));
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        printMatrix(result);
        System.out.println("Matrix multiplication execution time: " + duration + " ms.");
    }    

    public static int[][] multiplyMatrix(int[][] A, int[][] B) {
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

        return C;
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static List<int[][]> readMatrices(String filepath) throws IOException {
        List<int[][]> matrices = new ArrayList<>();
        List<int[]> matrix = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!matrix.isEmpty() && line.trim().isEmpty()) {
                    int[][] temp = new int[matrix.size()][];
                    for (int i = 0; i < matrix.size(); i++) {
                        temp[i] = matrix.get(i);
                    }
                    matrices.add(temp);
                    matrix.clear();
                }
                else {
                    String[] rowLine = line.trim().split("\\s+");
                    int[] row = new int[rowLine.length];
                    for (int i = 0; i < rowLine.length; i++) {
                        row[i] = Integer.parseInt(rowLine[i]);
                    }
                    matrix.add(row);
                }
            }
        }

        if (!matrix.isEmpty()) {
            int[][] temp = new int[matrix.size()][];
            for (int i = 0; i < matrix.size(); i++) {
                temp[i] = matrix.get(i);
            }
            matrices.add(temp);
        }

        return matrices;
    }
}
