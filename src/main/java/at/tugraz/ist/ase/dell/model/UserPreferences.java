/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPreferences {
    private double price_range = 0.0;
    private double weight = 0.0;
    private double processor = 0.0;
    private double memory = 0.0;
    private double hard_drive = 0.0;
    private double operating_system = 0.0;

    public boolean hasPreferences() {
        if (getPrice_range() > 0.0) return true;
        if (getWeight() > 0.0) return true;
        if (getProcessor() > 0.0) return true;
        if (getOperating_system() > 0.0) return true;
        if (getHard_drive() > 0.0) return true;
        return getMemory() > 0.0;
    }
}
