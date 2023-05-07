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

@Getter
public class ProductCatalog {
    List<Product> productsWithPrice = new LinkedList<>();

    public void addProduct(Product product) {
        productsWithPrice.add(product);
    }

    public double getProductPrice(Product product) {
        Product found = productsWithPrice.stream().filter(p -> p.equals(product))
                .findFirst().orElse(null);
        return found == null ? 0 : found.getTotal_price();
    }
}
