/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.model;

import at.tugraz.ist.ase.fm.core.Feature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class Option {
    String name;
    Feature feature;

    @Setter
    int contribution;

    public static Option newAllOption() {
        return Option.builder().name("(all)").feature(null).build();
    }

    public static boolean isNotAllOption(String optionName) {
        return !optionName.equals("(all)");
    }
}
