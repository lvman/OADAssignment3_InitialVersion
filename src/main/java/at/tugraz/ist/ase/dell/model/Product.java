/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.model;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Product implements Comparable<Product> {

    // category
    private String category;

    // price_range
    private String price_range;

    // weight
    private String weight;

    // processor
    private String processor;

    // operating_system
    private String operating_system;

    // hard drives
    private String hard_drive;

    // optical drive
    private String optical_drive;

    // memory
    private String memory;

    @EqualsAndHashCode.Exclude
    @Setter
    private double total_price;

    @Setter
    @EqualsAndHashCode.Exclude
    private int utility;

    /**
     * Compare two products by their total price
     * @param o the other product
     * @return 1 if this product is more expensive than the other product,
     *        -1 if this product is cheaper than the other product,
     *         0 if they are equal
     */
    @Override
    public int compareTo(Product o) {
        return Double.compare(this.total_price, o.total_price);
    }
}
