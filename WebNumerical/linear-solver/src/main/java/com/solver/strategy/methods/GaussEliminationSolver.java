package com.solver.strategy.methods;

import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.*;

public class GaussEliminationSolver implements LinearSolverStrategy {

    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.vectorB.length;
        SolveResponse res = new SolveResponse();

        // ── Guard: null / size check ──────────────────────────────────────────
        if (req.matrixA == null || req.vectorB == null || req.matrixA.length != n) {
            res.converged = false;
            res.message = "Invalid input: matrix and vector sizes do not match.";
            return res;
        }

        // ── Build Augmented Matrix [A | b] ────────────────────────────────────
        double[][] a = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = req.matrixA[i][j];
            }
            a[i][n] = req.vectorB[i];
        }

        // ── Forward Elimination with Partial Pivoting ─────────────────────────
        // Partial Pivoting: สลับแถวเพื่อให้ pivot element มีค่า absolute สูงสุด
        // ป้องกัน Division by Zero และลด Numerical Instability
        for (int i = 0; i < n; i++) {

            // หา pivot row: แถวที่มี |a[row][i]| มากที่สุดใน column i
            int pivotRow = i;
            double maxVal = Math.abs(a[i][i]);
            for (int row = i + 1; row < n; row++) {
                if (Math.abs(a[row][i]) > maxVal) {
                    maxVal = Math.abs(a[row][i]);
                    pivotRow = row;
                }
            }

            // Singular Matrix check: pivot ≈ 0 → ระบบสมการไม่มีคำตอบเดียว
            if (Math.abs(a[pivotRow][i]) < 1e-12) {
                res.converged = false;
                res.message = "Matrix is singular or nearly singular (pivot ≈ 0 at column " + i + ").";
                return res;
            }

            // สลับแถว i กับ pivotRow (ถ้า pivotRow ไม่ใช่แถวปัจจุบัน)
            if (pivotRow != i) {
                double[] temp = a[i];
                a[i] = a[pivotRow];
                a[pivotRow] = temp;
            }

            // Eliminate rows below pivot
            for (int j = i + 1; j < n; j++) {
                double ratio = a[j][i] / a[i][i];
                for (int k = i; k <= n; k++) {
                    a[j][k] -= ratio * a[i][k];
                }
            }
        }

        // ── Backward Substitution ─────────────────────────────────────────────
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = a[i][n];
            for (int j = i + 1; j < n; j++) {
                x[i] -= a[i][j] * x[j];
            }
            x[i] /= a[i][i];
        }

        res.finalSolution = x;
        res.converged = true;
        res.message = "Gaussian elimination with partial pivoting completed successfully.";
        return res;
    }
}
