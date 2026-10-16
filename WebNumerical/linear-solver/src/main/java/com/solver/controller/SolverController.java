package com.solver.controller;

import com.solver.dto.SolveRequest;
import com.solver.dto.SolveResponse;
import com.solver.strategy.SolverContext;
import com.solver.strategy.methods.CholeskySolver;
import com.solver.strategy.methods.ConjugateGradientSolver;
import com.solver.strategy.methods.CramerSolver;
import com.solver.strategy.methods.GaussEliminationSolver;
import com.solver.strategy.methods.GaussSeidelSolver;
import com.solver.strategy.methods.JacobiSolver;
import com.solver.strategy.methods.LUDecompositionSolver;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // อนุญาตให้เชื่อมต่อจากโดเมนอื่น (เช่น Live Server พอร์ต 5500)
public class SolverController {

    @PostMapping("/solve")
    public SolveResponse solveEquation(@RequestBody SolveRequest request) {
        SolverContext context = new SolverContext();
        
        // เลือก Strategy ของอัลกอริทึมตาม Method ที่ส่งมาจากหน้าเว็บ
        switch (request.method) {
            case "Jacobi":
                context.setStrategy(new JacobiSolver());
                break;
            case "GaussSeidel":
                context.setStrategy(new GaussSeidelSolver());
                break;
            case "Cramer":
                context.setStrategy(new CramerSolver());
                break;
            case "GaussElimination":
                context.setStrategy(new GaussEliminationSolver());
                break;
            case "LU":
                context.setStrategy(new LUDecompositionSolver());
                break;
            case "Cholesky":
                context.setStrategy(new CholeskySolver());
                break;
            case "ConjugateGradient":
                context.setStrategy(new ConjugateGradientSolver());
                break;
            default:
                throw new IllegalArgumentException("ไม่พบอัลกอริทึมที่เลือก (Unknown method): " + request.method);
        }
        
        // สั่งประมวลผลคำนวณแล้วคืนค่ากลับไปเป็น Response
        return context.execute(request);
    }
}