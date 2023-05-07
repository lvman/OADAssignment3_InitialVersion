/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.service;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.dell.model.DellKB;
import at.tugraz.ist.ase.kb.core.Assignment;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Configurator {

    protected final DellKB Kb;

    private final SolutionTranslator translator = new SolutionTranslator();

    @Getter
    protected final List<Solution> solutions = new LinkedList<>();

    @Getter
    protected Constraint requirement = null;

    protected Model model;
    protected Solver solver;

    public Configurator(@NonNull DellKB dellKb) {
        this.Kb = dellKb;

        this.model = dellKb.getChocoModel();
        this.solver = this.model.getSolver();
    }

    public int getNumberSolutions() {
        return solutions.size();
    }

    public Solution getLastestSolution() {
        if (solutions.isEmpty())
            return null;
        return solutions.get(getNumberSolutions() - 1);
    }

    public void emptySolutions() {
        solutions.clear();
    }

    /**
     * Add constraints to the Choco model before activating the solve() method.
     * @param C the constraints to add to the model.
     */
    protected void prepareSolver(Collection<Constraint> C) {
        C.forEach(c -> c.getChocoConstraints().forEach(model::post));
        log.trace("{}Posted constraints", LoggerUtils.tab());
    }

    public void setRequirement(@NonNull Requirement req) {
        // translate requirement to Constraint
        requirement = translator.translate(req, this.Kb.getKb());

        requirement.getChocoConstraints().forEach(model::post);
        log.trace("{}Posted requirement", LoggerUtils.tab());
    }

    public void removeRequirement() {
        if (requirement != null) {
            model.unpost(requirement.getChocoConstraints().toArray(new org.chocosolver.solver.constraints.Constraint[0]));
            log.trace("{}Unposted requirement", LoggerUtils.tab());
        }
    }

    public boolean find(int maxNumConf, long timeout) {
        // get the solver
        if (timeout > 0) {
            solver.limitTime(timeout);
            log.trace("{}Set timeout: {} ms", LoggerUtils.tab(), timeout);
        }

        // solver
        int numSolve = 0; // number of solve calls
        int numSolutions = 0;
        int numSimilar = 0;
        boolean could_more_solution = true;
        do {

            if (!solver.solve()) {
                could_more_solution = false;
            } else {
                numSolve++;
                Solution solution = getCurrentSolution();

                if (contains(solution)) {
                    numSimilar++;
                } else {
                    numSolutions++;
                    log.trace("{}{}. {}", LoggerUtils.tab(), numSolutions, solution);
                    solutions.add(solution);
                }
            }
        } while (could_more_solution && (maxNumConf == 0 || numSolutions < maxNumConf));
        log.trace("{}Solve executed {} times", LoggerUtils.tab(), numSolve);
        log.trace("{}Found {} similar solutions", LoggerUtils.tab(), numSimilar);

        return could_more_solution;
    }

    /**
     * Remove constraints from the Choco model after solving the model.
     */
    public void reset() {
        solver.hardReset();
        model.unpost(model.getCstrs()); // unpost all constraints

        log.trace("{}Reset model and unpost all constraints", LoggerUtils.tab());
    }

    private void prefindSolutions(Requirement requirement) {
        emptySolutions();
        prepareSolver(Kb.getConstraints());

        if (requirement != null) {
            setRequirement(requirement);
        }
    }

    public void findAllSolutions(long timeout) {
        prefindSolutions(null);

        find(0, timeout);

        // remove all constraints
        reset();
    }

    public void findAllSolutions(long timeout, Requirement requirement) {
        prefindSolutions(requirement);

        find(0, timeout);

        // remove all constraints
        reset();
    }

    protected Solution getCurrentSolution() {
        List<Assignment> assignments = Kb.getVariableList().stream()
                .map(var -> Assignment.builder()
                        .variable(var.getName())
                        .value(var.getValue())
                        .build())
                .collect(Collectors.toCollection(LinkedList::new));

        return Solution.builder().assignments(assignments).build();
    }

    private boolean contains(Solution solution) {
        return solutions.stream().anyMatch(s -> s.equals(solution));
    }
}
