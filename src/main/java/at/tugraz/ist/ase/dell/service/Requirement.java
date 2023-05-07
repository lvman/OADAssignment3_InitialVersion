/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2021-2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.service;

import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

public class Requirement extends Solution {
    @Builder(builderMethodName = "requirementBuilder")
    public Requirement(@NonNull List<Assignment> assignments) {
        super(assignments);
    }
}
