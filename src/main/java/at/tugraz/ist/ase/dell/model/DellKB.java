/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.model;

import at.tugraz.ist.ase.common.IOUtils;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.kb.core.Constraint;
import at.tugraz.ist.ase.kb.core.Variable;
import at.tugraz.ist.ase.kb.fm.FMKB;
import lombok.Cleanup;
import lombok.Getter;
import org.chocosolver.solver.Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DellKB {

    static final String FEATURE_MODEL_FILE = "src/main/resources/DELL.splx";
    static final String COMPONENT_UTILITY = "DELL_utility.csv";
    static final String PRODUCTS_PRICE = "DELL_price.csv";
    static final List<String> COMPONENT_IDS = List.of("price_range", "productcategory", "processor",
            "memory", "harddrive", "opticaldrive", "operatingsystem", "weight");
    @Getter
    static final List<String> RESULT_TABLE_COLUMN_NAMES = List.of("Category", "Processor",
            "Memory", "Hard Drive", "Optical Drive", "Operating System", "Weight", "Price", "Order");

    @Getter
    Model chocoModel;
    @Getter
    FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb;

    @Getter
    Set<Constraint> constraints = new LinkedHashSet<>();

    @Getter
    ProductStructure productStructure = new ProductStructure();

    @Getter
    ProductCatalog productCatalog = new ProductCatalog();

    public void init() throws FeatureModelParserException, IOException {
        // read the feature model from the file
        File fileFM = new File(FEATURE_MODEL_FILE);

        // create the factory for feature models
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint>
                factory = FMParserFactory.getInstance();

        // create the parser
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint>
                fm = parser.parse(fileFM);

        // translate the feature model into CSP
        kb = new FMKB<>(fm, false);
        chocoModel = kb.getModelKB(); // get the Choco model

        // initialize components
        initComponents(fm);

        // read component price
        loadProductsWithPrice();
        loadComponentUtility();

        // prepare the list of constraints - add the root constraint
        List<Constraint> B = new LinkedList<>(kb.getConstraintList());
        Constraint constraint = kb.getRootConstraint(); // get the root constraint
        B.add(0, constraint); // add the root constraint to the beginning of the list
        this.constraints = new LinkedHashSet<>(B);

        // remove all Choco constraints, cause we just need variables
        this.chocoModel.unpost(this.chocoModel.getCstrs());
    }

    private void loadProductsWithPrice() throws IOException {
        InputStream is = IOUtils.getInputStream(getClass().getClassLoader(), PRODUCTS_PRICE);
        @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        // ignore the header line
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            Product product = Product.builder()
                    .category(parts[0])
                    .price_range(parts[1])
                    .weight(parts[2])
                    .processor(parts[3])
                    .operating_system(parts[4])
                    .hard_drive(parts[5])
                    .optical_drive(parts[6])
                    .memory(parts[7])
                    .total_price(Double.parseDouble(parts[8]))
                    .build();

            productCatalog.addProduct(product);
        }
    }

    private void loadComponentUtility() throws IOException {
        InputStream is = IOUtils.getInputStream(getClass().getClassLoader(), COMPONENT_UTILITY);
        @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        // ignore the header line
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");

            String optionName = parts[0];
            int utility = Integer.parseInt(parts[1]);

            Option option = productStructure.getOption(optionName);
            option.setContribution(utility);
        }
    }

    private void initComponents(FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm) {
        for (String componentId : COMPONENT_IDS) {
            Feature feature = fm.getFeature(componentId);
            Component component = new Component(feature.getName(), feature);
            productStructure.addComponent(component);

            // add an empty option
            component.addOption(Option.newAllOption());

            feature.getChildren().forEach(child ->
                    component.addOption(Option.builder()
                            .name(removePrefix(child.getName()))
                            .feature(child)
                            .build()));
        }
    }

    private String removePrefix(String name) {
        if (name.startsWith("F_")) {
            return name.substring(name.indexOf("F_") + 2);
        }
        return name;
    }

    public List<Variable> getVariableList() {
        return kb.getVariableList();
    }
}
