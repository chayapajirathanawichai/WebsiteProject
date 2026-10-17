package com.solver.strategy.methods;

import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;

public class CholeskySolver implements LinearSolverStrategy {

    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.matrixA.length;
        SolveResponse res = new SolveResponse();

        // ── Guard: null / size check ──────────────────────────────────────────
        if (req.matrixA == null || req.vectorB == null || req.vectorB.length != n) {
            res.converged = false;
            res.message = "Invalid input: matrix and vector sizes do not match.";
            return res;
        }

        // ── 1. Symmetry check: A[i][j] == A[j][i] ────────────────────────────
        // Cholesky requires a strictly Symmetric Positive Definite (SPD) matrix.
        // Tolerance 1e-9 รองรับ floating-point rounding error เล็กน้อย
        final double SYM_TOL = 1e-9;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (Math.abs(req.matrixA[i][j] - req.matrixA[j][i]) > SYM_TOL) {
                    res.converged = false;
                    res.message = String.format(
                        "Matrix is not symmetric: A[%d][%d]=%.6f ≠ A[%d][%d]=%.6f",
                        i, j, req.matrixA[i][j], j, i, req.matrixA[j][i]);
                    return res;
                }
            }
        }

        // ── 2. Cholesky Decomposition: A = L * L^T ───────────────────────────
        double[][] lower = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0;

                if (j == i) {
                    // Diagonal element: L[j][j] = sqrt(A[j][j] - Σ L[j][k]^2)
                    for (int k = 0; k < j; k++) {
                        sum += lower[j][k] * lower[j][k];
                    }
                    double val = req.matrixA[j][j] - sum;

                    // Positive Definite check: diagonal of A − Σ must be > 0
                    if (val <= 0) {
                        res.converged = false;
                        res.message = "Matrix is not positive definite "
                            + "(encountered non-positive value on diagonal during decomposition).";
                        return res;
                    }
                    lower[j][j] = Math.sqrt(val);

                } else {
                    // Off-diagonal element: L[i][j] = (A[i][j] - Σ L[i][k]*L[j][k]) / L[j][j]
                    for (int k = 0; k < j; k++) {
                        sum += lower[i][k] * lower[j][k];
                    }
                    lower[i][j] = (req.matrixA[i][j] - sum) / lower[j][j];
                }
            }
        }

        // ── 3. Forward substitution: L * y = b ───────────────────────────────
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += lower[i][j] * y[j];
            }
            y[i] = (req.vectorB[i] - sum) / lower[i][i];
        }

        // ── 4. Backward substitution: L^T * x = y ────────────────────────────
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += lower[j][i] * x[j];  // L^T[i][j] = L[j][i]
            }
            x[i] = (y[i] - sum) / lower[i][i];
        }

        res.finalSolution = x;
        res.converged = true;
        res.message = "Cholesky decomposition completed successfully.";
        return res;
    }
}
