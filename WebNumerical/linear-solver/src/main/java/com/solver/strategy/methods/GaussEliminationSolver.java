package com.solver.strategy.methods;
import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.*;

public class GaussEliminationSolver implements LinearSolverStrategy{
    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.vectorB.length;
        double[][] a = new double[n][n+1];
        
        // สร้าง Augmented Matrix
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) a[i][j] = req.matrixA[i][j];
            a[i][n] = req.vectorB[i];
        }
        
        // Forward Elimination
        for(int i=0; i<n; i++) {
            for(int j=i+1; j<n; j++) {
                double ratio = a[j][i] / a[i][i];
                for(int k=0; k<=n; k++) {
                    a[j][k] = a[j][k] - ratio * a[i][k];
                }
            }
        }
        
        // Backward Substitution
        double[] x = new double[n];
        for(int i=n-1; i>=0; i--) {
            x[i] = a[i][n];
            for(int j=i+1; j<n; j++) {
                x[i] = x[i] - a[i][j] * x[j];
            }
            x[i] = x[i] / a[i][i];
        }
        
        SolveResponse res = new SolveResponse();
        res.finalSolution = x;
        res.converged = true;
        return res;
    }
}
