package com.solver.strategy.methods;

import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;

public class CholeskySolver implements LinearSolverStrategy{
    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.matrixA.length;
        double[][] lower = new double[n][n];
        SolveResponse res = new SolveResponse();

        // Decomposing a matrix into Lower Triangular
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0;
                if (j == i) { // Diagonal elements
                    for (int k = 0; k < j; k++) sum += Math.pow(lower[j][k], 2);
                    double val = req.matrixA[j][j] - sum;
                    if (val <= 0) {
                        res.converged = false;
                        res.message = "Matrix is not positive definite.";
                        return res;
                    }
                    lower[j][j] = Math.sqrt(val);
                } else { // Non-diagonal
                    for (int k = 0; k < j; k++) sum += (lower[i][k] * lower[j][k]);
                    lower[i][j] = (req.matrixA[i][j] - sum) / lower[j][j];
                }
            }
        }

        // L * Y = B (Forward substitution)
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) sum += lower[i][j] * y[j];
            y[i] = (req.vectorB[i] - sum) / lower[i][i];
        }

        // L^T * X = Y (Backward substitution)
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) sum += lower[j][i] * x[j];
            x[i] = (y[i] - sum) / lower[i][i];
        }

        res.finalSolution = x;
        res.converged = true;
        return res;
    }
}
