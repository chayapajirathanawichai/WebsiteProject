package com.solver.strategy.methods;

import com.solver.strategy.LinearSolverStrategy;
import com.solver.dto.IterationStep;
import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;
import java.util.ArrayList;

public class JacobiSolver implements LinearSolverStrategy{
    @Override
    public SolveResponse solve(SolveRequest req) {
        int n = req.matrixA.length;
        double[] x = req.initialGuess.clone();
        double[] xNew = new double[n];
        SolveResponse res = new SolveResponse();
        res.steps = new ArrayList<>();
        
        for (int iter = 1; iter <= req.maxIter; iter++) {
            double maxError = 0.0;
            
            for (int i = 0; i < n; i++) {
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (j != i) sum += req.matrixA[i][j] * x[j]; // ใช้ค่า X เก่าในการคำนวณรอบนี้
                }
                xNew[i] = (req.vectorB[i] - sum) / req.matrixA[i][i];
                
                double error = Math.abs((xNew[i] - x[i]) / xNew[i]) * 100;
                if (error > maxError) maxError = error;
            }
            
            x = xNew.clone(); // อัปเดตค่าทั้งหมดพร้อมกันเมื่อจบรอบ
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
