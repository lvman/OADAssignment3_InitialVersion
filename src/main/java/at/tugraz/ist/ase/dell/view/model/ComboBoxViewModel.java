/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.view.model;

import javax.swing.*;

public class ComboBoxViewModel extends DefaultComboBoxModel<String> {

    public ComboBoxViewModel(String[] values) {
        super(values);
    }

    @Override
    public String getSelectedItem() {
        return (String) super.getSelectedItem();
    }
}
