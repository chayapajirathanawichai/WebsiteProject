package com.solver.dto;

public class SolveRequest {
    public String method;
    public double[][] matrixA;
    public double[] vectorB;
    public double[] initialGuess;
    public double tolerance;
    public int maxIter;
}