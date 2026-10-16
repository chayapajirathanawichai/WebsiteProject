package com.solver.dto;
import java.util.List;

public class SolveResponse {
    public double[] finalSolution;
    public List<IterationStep> steps; // สำหรับวิธีทำซ้ำ
    public boolean converged;
    public String message;
}
