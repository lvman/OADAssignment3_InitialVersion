/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.mapper;

import at.tugraz.ist.ase.dell.model.Component;
import at.tugraz.ist.ase.dell.view.model.ComboBoxViewModel;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComponentMapper {
    public ComboBoxViewModel toComboBoxViewModel(@NonNull Component component) {
        return new ComboBoxViewModel(component.getOptionNames().toArray(new String[0]));
    }
}
