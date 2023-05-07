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
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.constraints.Constraint;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Solution implements Cloneable {
    @EqualsAndHashCode.Include
    protected List<Assignment> assignments;
    private List<Constraint> chocoConstraints;

    @Builder
    public Solution(@NonNull List<Assignment> assignments) {
        this.assignments = assignments;
        this.chocoConstraints = new LinkedList<>();

        log.trace("{}Created Solution [assignments={}]", LoggerUtils.tab(), assignments);
    }

    public Assignment getAssignment(@NonNull String varName) {
        checkArgument(!varName.isEmpty(), "Variable name cannot be empty!");

        for (Assignment assignment : assignments) {
            if (assignment.getVariable().equals(varName)) {
                return assignment;
            }
        }
        throw new IllegalArgumentException("Variable '" + varName + "' doesn't exist!");
    }

    /**
     * Adds a Choco constraint translated from this solution.
     * @param constraint a Choco constraint
     */
    public void addChocoConstraint(@NonNull Constraint constraint) {
        chocoConstraints.add(constraint);

        log.trace("{}Added a Choco constraint to Solution [choco_cstr={}, solution={}]", LoggerUtils.tab(), constraint, this);
    }

    public int size() {
        return assignments.size();
    }

    @Override
    public String toString() {
        return assignments.stream().map(Assignment::toString).collect(Collectors.joining(", "));
    }

    public Object clone() throws CloneNotSupportedException {
        Solution clone = (Solution) super.clone();
        // copy assignments
        List<Assignment> assignments = new LinkedList<>();
        for (Assignment assignment : this.assignments) {
            Assignment cloneAssignment = (Assignment) assignment.clone();
            assignments.add(cloneAssignment);
        }
        clone.assignments = assignments;

        return clone;
    }

    public void dispose() {
        if (chocoConstraints != null) {
            chocoConstraints.clear();
            chocoConstraints = null;
        }
        assignments = null;
    }
}
