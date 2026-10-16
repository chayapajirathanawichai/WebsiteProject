package com.solver.dto;

public class IterationStep {
    public int iteration;
    public double[] x;
    public double error;
    public IterationStep(int iter, double[] x, double error) {
        this.iteration = iter; this.x = x.clone(); this.error = error;
    }
}
