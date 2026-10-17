package com.solver.strategy;

import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;
import com.solver.strategy.methods.CholeskySolver;
import com.solver.strategy.methods.ConjugateGradientSolver;

public class SolverContext {
    private LinearSolverStrategy strategy;

    public void setStrategy(LinearSolverStrategy strategy) {
        this.strategy = strategy;
    }
    public SolveResponse execute(SolveRequest request) {
        return strategy.solve(request);
    }
}
