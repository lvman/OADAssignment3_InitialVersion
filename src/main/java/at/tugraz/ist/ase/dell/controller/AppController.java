/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.controller;

import at.tugraz.ist.ase.dell.mapper.ComponentMapper;
import at.tugraz.ist.ase.dell.mapper.ProductMapper;
import at.tugraz.ist.ase.dell.mapper.UserRequirementMapper;
import at.tugraz.ist.ase.dell.model.*;
import at.tugraz.ist.ase.dell.service.Configurator;
import at.tugraz.ist.ase.dell.service.Requirement;
import at.tugraz.ist.ase.dell.view.MainView;
import at.tugraz.ist.ase.dell.view.model.ComboBoxViewModel;
import at.tugraz.ist.ase.dell.view.model.TableViewModel;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import lombok.NonNull;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController {
    protected DellKB dellKb;
    protected UserRequirement userRequirement;
    protected JFrame mainView;

    protected List<Product> products = null;

    public void createAndShowMainView() throws FeatureModelParserException, IOException {
        // create the DellKB
        dellKb = new DellKB();
        dellKb.init();

        userRequirement = new UserRequirement();

        // use the Java look and feel.
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) { }

        // make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        mainView = new MainView(this, userRequirement);
        mainView.setLocationRelativeTo(null); //center the window
        mainView.setVisible(true);
    }

    public Map<String, ComboBoxViewModel> getComponentOptionsViewModels(@NonNull List<String> comboBoxNames) {
        Map<String, ComboBoxViewModel>  map = new HashMap<>();
        for (String comboboxName : comboBoxNames) {
            Component component = dellKb.getProductStructure().getComponent(comboboxName);
            map.put(comboboxName, ComponentMapper.toComboBoxViewModel(component));
        }
        return map;
    }

    public int findProducts(@NonNull UserRequirement userRequirement) {
        Configurator configurator = new Configurator(dellKb);

        // map to Requirement
        Requirement requirement = UserRequirementMapper.toRequirement(userRequirement, dellKb.getProductStructure());

        configurator.findAllSolutions(0, requirement);
//        System.out.println("Number of solutions: " + configurator.getNumberSolutions());

        products = ProductMapper.fromSolutions(configurator.getSolutions(), dellKb.getProductStructure(), dellKb.getProductCatalog());

        return products.size();
    }

//
// BEGIN OF TASK 2 - Strategy pattern
//
    public Map.Entry<Integer, TableViewModel> sortProducts(@NonNull UserRequirement userRequirement) {
        if (products.isEmpty()) {
            return Map.entry(0, new TableViewModel(new String[0], new Object[0][0]));
        }

        // check utility
        if (userRequirement.hasPreferences()) {
            // calculate utility
            products.forEach(product -> product.setUtility(calculateUtility(product, userRequirement)));
            DescendingUtilityComparator comparator = new DescendingUtilityComparator();
            products.sort(comparator);
        } else {
            products.sort(Product::compareTo);
        }

        TableViewModel tableViewModel = ProductMapper.toTableViewModel(products, DellKB.getRESULT_TABLE_COLUMN_NAMES());
        return Map.entry(products.size(), tableViewModel);
    }

    private int calculateUtility(Product product, UserRequirement userRequirement) {
        int utility = 0;

        // price range
        if (userRequirement.getPref_price_range() > 0) {
            String priceRange = product.getPrice_range();
            if (!priceRange.isEmpty()) {
                utility += userRequirement.getPref_price_range() * dellKb.getProductStructure().getOption(priceRange).getContribution();
            }
        }

        // weight
        if (userRequirement.getPref_weight() > 0) {
            String weight = product.getWeight();
            if (!weight.isEmpty()) {
                utility += userRequirement.getPref_weight() * dellKb.getProductStructure().getOption(weight).getContribution();
            }
        }

        // processor
        if (userRequirement.getPref_processor() > 0) {
            String processor = product.getProcessor();
            if (!processor.isEmpty()) {
                utility += userRequirement.getPref_processor() * dellKb.getProductStructure().getOption(processor).getContribution();
            }
        }

        // memory
        if (userRequirement.getPref_memory() > 0) {
            String memory = product.getMemory();
            if (!memory.isEmpty()) {
                utility += userRequirement.getPref_memory() * dellKb.getProductStructure().getOption(memory).getContribution();
            }
        }

        // hard drive
        if (userRequirement.getPref_hard_drive() > 0) {
            String hardDrive = product.getHard_drive();
            if (!hardDrive.isEmpty()) {
                utility += userRequirement.getPref_hard_drive() * dellKb.getProductStructure().getOption(hardDrive).getContribution();
            }
        }

        // operating system
        if (userRequirement.getPref_operating_system() > 0) {
            String operatingSystem = product.getOperating_system();
            if (!operatingSystem.isEmpty()) {
                utility += userRequirement.getPref_operating_system() * dellKb.getProductStructure().getOption(operatingSystem).getContribution();
            }
        }

        return utility;
    }
//
// END OF TASK 2 - Strategy pattern
//
}
