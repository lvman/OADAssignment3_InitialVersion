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
public class UserNeeds {
    private String price_range = "";
    private String weight = "";
    private String processor = "";
    private String memory = "";
    private String hard_drive = "";
    private String operating_system = "";
}
