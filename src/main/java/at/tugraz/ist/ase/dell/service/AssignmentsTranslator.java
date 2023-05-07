/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.service;

import at.tugraz.ist.ase.common.ChocoSolverUtils;
import at.tugraz.ist.ase.kb.core.Assignment;
import at.tugraz.ist.ase.kb.core.KB;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

import java.util.List;

public class AssignmentsTranslator {

    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignments the {@link Assignment}s to translate
     * @param kb the {@link KB}
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     */
    public void translate(@NonNull List<Assignment> assignments, @NonNull KB kb,
                          @NonNull List<Constraint> chocoCstrs) {
        if (assignments.isEmpty()) {
            return;
        }

        int startIdx = kb.getNumChocoConstraints();
        Model model = kb.getModelKB();

        LogOp logOp = create(assignments, kb);
        post(logOp, model, chocoCstrs, startIdx);
    }

    private static void post(LogOp logOp, Model model, List<Constraint> chocoCstrs, int startIdx) {
        model.addClauses(logOp); // add the translated constraints to the Choco kb

        List<Constraint> postedCstrs = ChocoSolverUtils.getConstraints(model, startIdx, model.getNbCstrs() - 1);

        chocoCstrs.addAll(postedCstrs);

        // remove the posted constraints from the Choco model
        postedCstrs.forEach(model::unpost);
    }

    public LogOp create(@NonNull List<Assignment> assignments, @NonNull KB kb) {
        LogOp logOp = LogOp.and(); // creates a AND LogOp
        for (Assignment assignment : assignments) { // get each clause
            ChocoSolverUtils.addAssignmentToLogOp(logOp, kb.getModelKB(), assignment.getVariable(), assignment.getValue());
        }
        return logOp;
    }

    public LogOp create(@NonNull Assignment assignment, @NonNull KB kb) {
        LogOp logOp = LogOp.and(); // creates a AND LogOp
        ChocoSolverUtils.addAssignmentToLogOp(logOp, kb.getModelKB(), assignment.getVariable(), assignment.getValue());
        return logOp;
    }
}
