package com.solver.strategy.methods;

import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.IterationStep;
import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;
import java.util.ArrayList;

public class ConjugateGradientSolver implements LinearSolverStrategy{
    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.matrixA.length;
        double[] x = req.initialGuess.clone();
        double[] r = new double[n];
        double[] p = new double[n];
        
        SolveResponse res = new SolveResponse();
        res.steps = new ArrayList<>();

        // r0 = b - A*x0
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) sum += req.matrixA[i][j] * x[j];
            r[i] = req.vectorB[i] - sum;
            p[i] = r[i];
        }

        double rsold = dotProduct(r, r);
        
        for (int iter = 1; iter <= req.maxIter; iter++) {
            double[] Ap = multiplyMatrixVector(req.matrixA, p);
            double alpha = rsold / dotProduct(p, Ap);
            
            double maxError = 0;
            double[] xOld = x.clone();

            for (int i = 0; i < n; i++) {
                x[i] = x[i] + alpha * p[i];
                r[i] = r[i] - alpha * Ap[i];
                
                // คำนวณ Error เชิงสัมพันธ์ (Relative Error)
                if (x[i] != 0) {
                    double err = Math.abs((x[i] - xOld[i]) / x[i]) * 100;
                    if (err > maxError) maxError = err;
                }
            }

            res.steps.add(new IterationStep(iter, x.clone(), maxError));

            if (Math.sqrt(dotProduct(r, r)) < 1e-10 || maxError < req.tolerance) {
                res.converged = true;
                break;
            }

            double rsnew = dotProduct(r, r);
            for (int i = 0; i < n; i++) {
                p[i] = r[i] + (rsnew / rsold) * p[i];
            }
            rsold = rsnew;
        }
        
        res.finalSolution = x;
        return res;
    }

    private double dotProduct(double[] v1, double[] v2) {
        double sum = 0;
        for (int i = 0; i < v1.length; i++) sum += v1[i] * v2[i];
        return sum;
    }

    private double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        int n = matrix.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }
}
