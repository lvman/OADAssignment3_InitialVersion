/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.mapper;

import at.tugraz.ist.ase.dell.model.ProductStructure;
import at.tugraz.ist.ase.dell.model.UserRequirement;
import at.tugraz.ist.ase.dell.service.Requirement;
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;

@UtilityClass
public class UserRequirementMapper {
    public Requirement toRequirement(UserRequirement userRequirement, ProductStructure productStructure) {
        // create a list of assignments from the product
        List<Assignment> assignments = new LinkedList<>();
        if (!userRequirement.getPrice_range().isEmpty()) {
            String realName = productStructure.getRealName(userRequirement.getPrice_range());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userRequirement.getWeight().isEmpty()) {
            String realName = productStructure.getRealName(userRequirement.getWeight());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userRequirement.getOperating_system().isEmpty()) {
            String realName = productStructure.getRealName(userRequirement.getOperating_system());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userRequirement.getMemory().isEmpty()) {
            String realName = productStructure.getRealName(userRequirement.getMemory());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userRequirement.getHard_drive().isEmpty()) {
            String realName = productStructure.getRealName(userRequirement.getHard_drive());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userRequirement.getProcessor().isEmpty()) {
            String realName = productStructure.getRealName(userRequirement.getProcessor());
            assignments.add(new Assignment(realName, "true"));
        }

        return Requirement.requirementBuilder()
                .assignments(assignments)
                .build();
    }
}
