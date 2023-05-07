/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.mapper;

import at.tugraz.ist.ase.dell.model.Option;
import at.tugraz.ist.ase.dell.model.Product;
import at.tugraz.ist.ase.dell.model.ProductCatalog;
import at.tugraz.ist.ase.dell.model.ProductStructure;
import at.tugraz.ist.ase.dell.service.Solution;
import at.tugraz.ist.ase.dell.view.model.TableViewModel;
import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductMapper {

    public TableViewModel toTableViewModel(List<Product> products, List<String> columnNames) {
        if (products == null || products.isEmpty()) {
            return new TableViewModel(new String[0], new Object[0][0]);
        }

        Object[][] data = new Object[products.size()][9];
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = product.getCategory();
            data[i][1] = product.getProcessor();
            data[i][2] = product.getMemory();
            data[i][3] = product.getHard_drive();
            data[i][4] = product.getOptical_drive();
            data[i][5] = product.getOperating_system();
            data[i][6] = product.getWeight();
            data[i][7] = formatter.format(product.getTotal_price());
            data[i][8] = "Order";
        }

        return new TableViewModel(columnNames.toArray(new String[0]), data);
    }

    public Product fromSolution(Solution solution, ProductStructure productStructure, ProductCatalog productCatalog) {
        String category = getSelectedOption(solution, "Category", productStructure);
        String price_range = getSelectedOption(solution, "Price Range", productStructure);
        String weight = getSelectedOption(solution, "Weight", productStructure);
        String operating_system = getSelectedOption(solution, "Operating System", productStructure);
        String hard_drive = getSelectedOption(solution, "Hard Drive", productStructure);
        String optical_drive = getSelectedOption(solution, "Optical Drive", productStructure);
        String memory = getSelectedOption(solution, "Memory", productStructure);
        String processor = getSelectedOption(solution, "Processor", productStructure);

        Product product = Product.builder()
                .category(category)
                .price_range(price_range)
                .weight(weight)
                .operating_system(operating_system)
                .hard_drive(hard_drive)
                .optical_drive(optical_drive)
                .memory(memory)
                .processor(processor)
                .build();

        double total_price = productCatalog.getProductPrice(product);
        product.setTotal_price(total_price);

        return product;
    }

    public List<Product> fromSolutions(List<Solution> solutions, ProductStructure productStructure, ProductCatalog productCatalog) {
        return solutions.stream().map(solution -> fromSolution(solution, productStructure, productCatalog)).collect(Collectors.toList());
    }

    private String getSelectedOption(Solution solution, String componentName, ProductStructure productStructure) {
        for (String option : productStructure.getOptions(componentName)) {
            if (Option.isNotAllOption(option)) {
                String option_realName = productStructure.getRealName(option);
                if (solution.getAssignment(option_realName).getValue().equals("true")) {
                    return option;
                }
            }
        }
        return "";
    }
}
