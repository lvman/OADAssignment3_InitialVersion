/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell;

import at.tugraz.ist.ase.dell.controller.AppController;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FeatureModelParserException, IOException {
        AppController appController = new AppController();
        appController.createAndShowMainView();
    }
}
