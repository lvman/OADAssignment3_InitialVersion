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
import at.tugraz.ist.ase.dell.model.UserNeeds;
import at.tugraz.ist.ase.dell.service.Requirement;
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;

@UtilityClass
public class UserNeedsMapper {
    public Requirement toRequirement(UserNeeds userNeeds, ProductStructure productStructure) {
        // create a list of assignments from the product
        List<Assignment> assignments = new LinkedList<>();
        if (!userNeeds.getPrice_range().isEmpty()) {
            String realName = productStructure.getRealName(userNeeds.getPrice_range());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userNeeds.getWeight().isEmpty()) {
            String realName = productStructure.getRealName(userNeeds.getWeight());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userNeeds.getOperating_system().isEmpty()) {
            String realName = productStructure.getRealName(userNeeds.getOperating_system());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userNeeds.getMemory().isEmpty()) {
            String realName = productStructure.getRealName(userNeeds.getMemory());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userNeeds.getHard_drive().isEmpty()) {
            String realName = productStructure.getRealName(userNeeds.getHard_drive());
            assignments.add(new Assignment(realName, "true"));
        }
        if (!userNeeds.getProcessor().isEmpty()) {
            String realName = productStructure.getRealName(userNeeds.getProcessor());
            assignments.add(new Assignment(realName, "true"));
        }

        return Requirement.requirementBuilder()
                .assignments(assignments)
                .build();
    }
}
