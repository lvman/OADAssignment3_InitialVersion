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

import java.util.LinkedList;
import java.util.List;

public class ProductStructure {
    @Getter
    List<Component> components = new LinkedList<>();

    public void addComponent(Component component) {
        components.add(component);
    }

    public Component getComponent(String componentName) {
        for (Component component : components) {
            if (component.getName().equals(componentName)) {
                return component;
            }
        }
        return null;
    }

    public List<String> getOptions(String componentName) {
        Component component = getComponent(componentName);
        return component == null ? null : component.getOptionNames();
    }

    public Option getOption(String optionName) {
        for (Component component : components) {
            for (Option option : component.getOptions()) {
                if (option.getName().equals(optionName)) {
                    return option;
                }
            }
        }
        return null;
    }

    public String getRealName(String optionName) {
        for (Component component : components) {
            for (Option option : component.getOptions()) {
                if (option.getName().equals(optionName)) {
                    return option.getFeature().getName();
                }
            }
        }
        return null;
    }
}
