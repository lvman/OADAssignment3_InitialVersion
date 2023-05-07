/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.view;

import at.tugraz.ist.ase.dell.controller.AppController;
import at.tugraz.ist.ase.dell.model.Option;
import at.tugraz.ist.ase.dell.model.UserRequirement;
import at.tugraz.ist.ase.dell.view.model.ComboBoxViewModel;
import at.tugraz.ist.ase.dell.view.model.TableViewModel;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainView extends JFrame implements ActionListener {
    private static final String PREFERENCES_LABEL1_TEMPLATE_1 = "%d products have been found, that satisfy your needs.";
    private static final String PREFERENCES_LABEL2_TEMPLATE_1 = "Please tell us your preferences to rank identified products by utility.";
    private static final String PREFERENCES_LABEL3_TEMPLATE_1 = "If you don't specify your preferences, then products are sorted by price.";
    private static final String PREFERENCES_LABEL1_TEMPLATE_2 = "No product has been found, that satisfy your needs.";
    private static final String PREFERENCES_LABEL2_TEMPLATE_2 = "Please back to the previous step and change your needs.";

    private static final String RESULTS_LABEL1_TEMPLATE = "%d products have been found, and sorted by %s.";

    private JPanel panel;
    private JTabbedPane tabs;
    private JComboBox<String> cmbPrice;
    private JComboBox<String> cmbWeight;
    private JComboBox<String> cmbOperatingSystem;
    private JComboBox<String> cmbProcessor;
    private JComboBox<String> cmbMemory;
    private JComboBox<String> cmbHardDrive;
    private JButton btnBack;
    private JButton btnNext;
    private JTable tblResults;
    private JPanel tabNeeds;
    private JPanel tabPreferences;
    private JPanel tabResults;
    private JLabel lblPref_Label1;
    private JSpinner spnOperatingSystem;
    private JSpinner spnHardDrive;
    private JSpinner spnMemory;
    private JSpinner spnProcessor;
    private JSpinner spnPriceRange;
    private JSpinner spnWeight;
    private JLabel lblResults_Label;
    private JButton btnClear;
    private JLabel lblPref_Label2;
    private JLabel lblPref_Label3;

    private final AppController controller;
    private UserRequirement userRequirement;

    public MainView(@NonNull AppController controller,
                    @NonNull UserRequirement userRequirement) {
        super("DELL configurator");

        this.controller = controller;
        this.userRequirement = userRequirement;

        initComponents(controller);

        showNeedsTab();
    }

    private void initComponents(AppController controller) {
        List<JComboBox<String>> cmbComponents = List.of(cmbPrice, cmbWeight, cmbProcessor, cmbMemory, cmbHardDrive, cmbOperatingSystem);
        List<String> componentNames = cmbComponents.stream().map(Component::getName).collect(Collectors.toList());

        Map<String, ComboBoxViewModel> map = controller.getComponentOptionsViewModels(componentNames);

        cmbComponents.forEach(cmb -> {
            String componentName = cmb.getName();
            ComboBoxViewModel comboBoxViewModel = map.get(componentName);
            cmb.setModel(comboBoxViewModel);
        });

        spnPriceRange.setModel(new SpinnerNumberModel(0, 0, 100, 5));
        spnWeight.setModel(new SpinnerNumberModel(0, 0, 100, 5));
        spnProcessor.setModel(new SpinnerNumberModel(0, 0, 100, 5));
        spnMemory.setModel(new SpinnerNumberModel(0, 0, 100, 5));
        spnHardDrive.setModel(new SpinnerNumberModel(0, 0, 100, 5));
        spnOperatingSystem.setModel(new SpinnerNumberModel(0, 0, 100, 5));

        btnBack.addActionListener(this);
        btnNext.addActionListener(this);
        btnClear.addActionListener(this);

        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Next" -> next();
            case "Back" -> back();
            case "Reset" -> resetPreferences();
        }
    }

    private void resetPreferences() {
        spnPriceRange.setValue(0);
        spnWeight.setValue(0);
        spnProcessor.setValue(0);
        spnMemory.setValue(0);
        spnHardDrive.setValue(0);
        spnOperatingSystem.setValue(0);
    }

//
// BEGIN OF TASK 1 - State strategy
//
    enum TABS {NEEDS, PREFERENCES, RESULTS}
    private TABS currentTab; // keeps track of the current tab

    /**
     * Shows the Needs tab.
     */
    private void showNeedsTab() {
        currentTab = TABS.NEEDS; // the Needs tab is the current tab

        btnBack.setEnabled(false); // disable the Back button
        btnNext.setEnabled(true); // enable the Next button

        // disable all tabs
        IntStream.range(0, tabs.getComponentCount()).forEach(i -> tabs.setEnabledAt(i, false));
        // enable the tab Needs
        int index = tabs.indexOfComponent(tabNeeds);
        tabs.setEnabledAt(index, true);
        tabs.setSelectedIndex(index);
    }

    /**
     * Shows the Preferences tab.
     */
    private void showPreferencesTab() {
        currentTab = TABS.PREFERENCES; // the Preferences tab is the current tab

        btnBack.setEnabled(true); // enable the Back button
        btnNext.setEnabled(true); // enable the Next button

        // disable all tabs
        IntStream.range(0, tabs.getComponentCount()).forEach(i -> tabs.setEnabledAt(i, false));
        // enable the tab Needs
        int index = tabs.indexOfComponent(tabPreferences);
        tabs.setEnabledAt(index, true);
        tabs.setSelectedIndex(index);
    }

    /**
     * Shows the Results tab.
     */
    private void showResultsTab() {
        currentTab = TABS.RESULTS; // the Results tab is the current tab

        btnBack.setEnabled(true); // enable the Back button
        btnNext.setEnabled(false); // disable the Next button

        // disable all tabs
        IntStream.range(0, tabs.getComponentCount()).forEach(i -> tabs.setEnabledAt(i, false));
        // enable the tab Needs
        int index = tabs.indexOfComponent(tabResults);
        tabs.setEnabledAt(index, true);
        tabs.setSelectedIndex(index);
    }

    /**
     * The function is called when the user clicks the Next button.
     */
    private void next() {
        if (currentTab == TABS.NEEDS) { // if the current tab is the Needs tab
            // get user needs
            userRequirement = buildUserRequirement(userRequirement);

            // identify products that meet the user needs
            int numOfProducts = controller.findProducts(userRequirement);
            // update labels on the Preferences tab
            updateLabelsOnPreferencesTab(numOfProducts);
            // show the Preferences tab
            showPreferencesTab();
        } else if (currentTab == TABS.PREFERENCES) { // if the current tab is the Preferences tab
            // get user preferences
            userRequirement = buildUserPreferences(userRequirement);

            // sort products
            Map.Entry<Integer, TableViewModel> results = controller.sortProducts(userRequirement);
            int numberOfResults = results.getKey();
            TableViewModel model = results.getValue();

            if (numberOfResults > 0) {
                tblResults.setModel(model); // update the table

                // add action to the Order button
                // i.e., when the user clicks the Order button, prompt the product is ordered
                Action order = new AbstractAction("Order") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTable table = (JTable) e.getSource();
                        int row = Integer.parseInt(e.getActionCommand());
                        TableViewModel model = (TableViewModel) table.getModel();
                        String category = model.getValueAt(row, 0).toString();
                        String processor = model.getValueAt(row, 1).toString();
                        String memory = model.getValueAt(row, 2).toString();
                        String hardDrive = model.getValueAt(row, 3).toString();
                        String opticalDrive = model.getValueAt(row, 4).toString();
                        String operatingSystem = model.getValueAt(row, 5).toString();
                        String template = "Product %s with %s, %s memory, %s hard drive, %s ordered";
                        String withoutOpticalDrive_template = "and %s";
                        String withOpticalDrive_template = "%s, and %s";

                        // display a message
                        JOptionPane.showMessageDialog(MainView.this,
                                String.format(template, category, processor, memory, hardDrive,
                                        opticalDrive.isEmpty() ? String.format(withoutOpticalDrive_template, operatingSystem)
                                                : String.format(withOpticalDrive_template, operatingSystem, opticalDrive)));
                    }
                };
                ButtonColumn orderColumn = new ButtonColumn(tblResults, order, 8);
            }
            // update labels on the Results tab
            updateLabelsOnResultsTab(numberOfResults, userRequirement.hasPreferences());
            // show the Results tab
            showResultsTab();
        }
    }

    private void back() {
        if (currentTab == TABS.PREFERENCES) { // if the current tab is the Preferences tab
            showNeedsTab(); // show the Needs tab
        } else if (currentTab == TABS.RESULTS) { // if the current tab is the Results tab
            showPreferencesTab(); // show the Preferences tab
        }
    }

    private void updateLabelsOnPreferencesTab(int numberOfResults) {
        if (numberOfResults == 0) { // if there is no product that meets the user needs
            lblPref_Label1.setText(PREFERENCES_LABEL1_TEMPLATE_2);
            lblPref_Label2.setText(PREFERENCES_LABEL2_TEMPLATE_2);
            lblPref_Label3.setVisible(false);
        } else {
            lblPref_Label1.setText(String.format(PREFERENCES_LABEL1_TEMPLATE_1, numberOfResults));
            lblPref_Label2.setText(PREFERENCES_LABEL2_TEMPLATE_1);
            lblPref_Label3.setVisible(true);
            lblPref_Label3.setText(PREFERENCES_LABEL3_TEMPLATE_1);
        }
    }

    private void updateLabelsOnResultsTab(int numberOfResults, boolean hasPreferences) {
        if (hasPreferences) { // sort products by utility
            lblResults_Label.setText(String.format(RESULTS_LABEL1_TEMPLATE, numberOfResults, "utility"));
        } else { // sort products by price
            lblResults_Label.setText(String.format(RESULTS_LABEL1_TEMPLATE, numberOfResults, "price"));
        }
    }
//
// END OF TASK 1 - State strategy
//

    public UserRequirement buildUserRequirement(UserRequirement userRequirement) {
        String selectedPrice_Range = "";
        if (Option.isNotAllOption(Objects.requireNonNull(cmbPrice.getSelectedItem()).toString())) {
            selectedPrice_Range = Objects.requireNonNull(cmbPrice.getSelectedItem()).toString();
        }
        userRequirement.setPrice_range(selectedPrice_Range);

        String selectedWeight = "";
        if (Option.isNotAllOption(Objects.requireNonNull(cmbWeight.getSelectedItem()).toString())) {
            selectedWeight = Objects.requireNonNull(cmbWeight.getSelectedItem()).toString();
        }
        userRequirement.setWeight(selectedWeight);

        String selectedProcessor = "";
        if (Option.isNotAllOption(Objects.requireNonNull(cmbProcessor.getSelectedItem()).toString())) {
            selectedProcessor = Objects.requireNonNull(cmbProcessor.getSelectedItem()).toString();
        }
        userRequirement.setProcessor(selectedProcessor);

        String selectedMemory = "";
        if (Option.isNotAllOption(Objects.requireNonNull(cmbMemory.getSelectedItem()).toString())) {
            selectedMemory = Objects.requireNonNull(cmbMemory.getSelectedItem()).toString();
        }
        userRequirement.setMemory(selectedMemory);

        String selectedHardDrive = "";
        if (Option.isNotAllOption(Objects.requireNonNull(cmbHardDrive.getSelectedItem()).toString())) {
            selectedHardDrive = Objects.requireNonNull(cmbHardDrive.getSelectedItem()).toString();
        }
        userRequirement.setHard_drive(selectedHardDrive);

        String selectedOperatingSystem = "";
        if (Option.isNotAllOption(Objects.requireNonNull(cmbOperatingSystem.getSelectedItem()).toString())) {
            selectedOperatingSystem = Objects.requireNonNull(cmbOperatingSystem.getSelectedItem()).toString();
        }
        userRequirement.setOperating_system(selectedOperatingSystem);

        return userRequirement;
    }

    public UserRequirement buildUserPreferences(UserRequirement userRequirement) {
        double prefPriceRange = Double.parseDouble(spnPriceRange.getValue().toString()) / 100;
        if (prefPriceRange > 0.0) {
            userRequirement.setPref_price_range(prefPriceRange);
        }

        double prefWeight = Double.parseDouble(spnWeight.getValue().toString()) / 100;
        if (prefWeight > 0.0) {
            userRequirement.setPref_weight(prefWeight);
        }

        double prefProcessor = Double.parseDouble(spnProcessor.getValue().toString()) / 100;
        if (prefProcessor > 0.0) {
            userRequirement.setPref_processor(prefProcessor);
        }

        double prefMemory = Double.parseDouble(spnMemory.getValue().toString()) / 100;
        if (prefMemory > 0.0) {
            userRequirement.setPref_memory(prefMemory);
        }

        double prefHardDrive = Double.parseDouble(spnHardDrive.getValue().toString()) / 100;
        if (prefHardDrive > 0.0) {
            userRequirement.setPref_hard_drive(prefHardDrive);
        }

        double prefOperatingSystem = Double.parseDouble(spnOperatingSystem.getValue().toString()) / 100;
        if (prefOperatingSystem > 0.0) {
            userRequirement.setPref_operating_system(prefOperatingSystem);
        }

        return userRequirement;
    }
}
