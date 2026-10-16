package com.solver.strategy.methods;
import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.*;
import java.util.ArrayList;

public class GaussSeidelSolver implements LinearSolverStrategy{
    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.matrixA.length;
        double[] x = req.initialGuess.clone();
        SolveResponse res = new SolveResponse();
        res.steps = new ArrayList<>();
        
        for (int iter = 1; iter <= req.maxIter; iter++) {
            double maxError = 0.0;
            double[] xOld = x.clone();
            
            for (int i = 0; i < n; i++) {
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (j != i) sum += req.matrixA[i][j] * x[j]; // อัปเดต x ทันทีที่คำนวณได้
                }
                x[i] = (req.vectorB[i] - sum) / req.matrixA[i][i];
                
                double error = Math.abs((x[i] - xOld[i]) / x[i]) * 100;
                if (error > maxError) maxError = error;
            }
            
            res.steps.add(new IterationStep(iter, x.clone(), maxError));
            if (maxError < req.tolerance) {
                res.converged = true;
                break;
            }
        }
        res.finalSolution = x;
        return res;
    }
}
