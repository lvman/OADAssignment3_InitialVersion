/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2022-2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.service;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import at.tugraz.ist.ase.kb.core.KB;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SolutionTranslator {

    protected AssignmentsTranslator translator = new AssignmentsTranslator();

    /**
     * Translates an FM solution to Constraint
     */
    public Constraint translate(@NonNull Solution solution, @NonNull KB kb) {
        log.trace("{}Translating solution [solution={}] >>>", LoggerUtils.tab(), solution);
        Constraint constraint = new Constraint(solution.toString());

        translator.translate(solution.getAssignments(), kb,
                constraint.getChocoConstraints());

        // copy the generated constraints to Solution
        constraint.getChocoConstraints().forEach(solution::addChocoConstraint);

        log.debug("{}Translated solution [solution={}] >>>", LoggerUtils.tab(), solution);
        return constraint;
    }
}
