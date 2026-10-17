package com.solver.strategy;
import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;

public interface LinearSolverStrategy {
    SolveResponse solve(SolveRequest request);
} 