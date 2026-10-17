package com.solver.strategy.methods;

import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;

public class LUDecompositionSolver implements LinearSolverStrategy{
    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.matrixA.length;
        double[][] lower = new double[n][n];
        double[][] upper = new double[n][n];
        
        // Decomposing matrix into Lower and Upper triangular matrix
        for (int i = 0; i < n; i++) {
            // Upper
            for (int k = i; k < n; k++) {
                double sum = 0;
                for (int j = 0; j < i; j++) sum += (lower[i][j] * upper[j][k]);
                upper[i][k] = req.matrixA[i][k] - sum;
            }
            // Lower
            for (int k = i; k < n; k++) {
                if (i == k) lower[i][i] = 1; // Diagonal as 1
                else {
                    double sum = 0;
                    for (int j = 0; j < i; j++) sum += (lower[k][j] * upper[j][i]);
                    lower[k][i] = (req.matrixA[k][i] - sum) / upper[i][i];
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

        // U * X = Y (Backward substitution)
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) sum += upper[i][j] * x[j];
            x[i] = (y[i] - sum) / upper[i][i];
        }

        SolveResponse res = new SolveResponse();
        res.finalSolution = x;
        res.converged = true;
        return res;
    }
}
