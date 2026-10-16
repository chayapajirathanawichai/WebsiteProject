package com.solver.strategy.methods;

import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;

public class CramerSolver implements LinearSolverStrategy{
    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.matrixA.length;
        double detA = determinant(req.matrixA, n);
        
        SolveResponse res = new SolveResponse();
        
        if (Math.abs(detA) < 1e-10) {
            res.converged = false;
            res.message = "Matrix is singular (Determinant is 0).";
            return res;
        }

        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            double[][] modifiedMatrix = replaceColumn(req.matrixA, req.vectorB, i, n);
            x[i] = determinant(modifiedMatrix, n) / detA;
        }

        res.finalSolution = x;
        res.converged = true;
        return res;
    }

    private double determinant(double[][] matrix, int n) {
        if (n == 1) return matrix[0][0];
        if (n == 2) return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);

        double det = 0;
        int sign = 1;
        for (int f = 0; f < n; f++) {
            double[][] subMatrix = new double[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                int colCount = 0;
                for (int j = 0; j < n; j++) {
                    if (j == f) continue;
                    subMatrix[i - 1][colCount] = matrix[i][j];
                    colCount++;
                }
            }
            det += sign * matrix[0][f] * determinant(subMatrix, n - 1);
            sign = -sign;
        }
        return det;
    }

    private double[][] replaceColumn(double[][] matrix, double[] vectorB, int colIndex, int n) {
        double[][] newMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == colIndex) {
                    newMatrix[i][j] = vectorB[i];
                } else {
                    newMatrix[i][j] = matrix[i][j];
                }
            }
        }
        return newMatrix;
    }
}
