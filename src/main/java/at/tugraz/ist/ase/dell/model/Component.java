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
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Component {

    String name;
    Feature feature; // just backup
    List<Option> options = new LinkedList<>();

    @Builder
    public Component(String name, Feature feature) {
        this.name = name;
        this.feature = feature;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public List<String> getOptionNames() {
        return options.stream().map(Option::getName).collect(java.util.stream.Collectors.toList());
    }

}
