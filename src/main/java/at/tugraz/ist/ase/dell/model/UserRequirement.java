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
public class UserRequirement {
    private String price_range = "";
    private String weight = "";
    private String processor = "";
    private String memory = "";
    private String hard_drive = "";
    private String operating_system = "";

    private double pref_price_range = 0.0;
    private double pref_weight = 0.0;
    private double pref_processor = 0.0;
    private double pref_memory = 0.0;
    private double pref_hard_drive = 0.0;
    private double pref_operating_system = 0.0;

    public boolean hasPreferences() {
        if (getPref_price_range() > 0.0) return true;
        if (getPref_weight() > 0.0) return true;
        if (getPref_processor() > 0.0) return true;
        if (getPref_operating_system() > 0.0) return true;
        if (getPref_hard_drive() > 0.0) return true;
        return getPref_memory() > 0.0;
    }
}
