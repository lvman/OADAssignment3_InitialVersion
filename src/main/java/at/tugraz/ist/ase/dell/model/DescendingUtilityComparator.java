/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.model;

import java.util.Comparator;

public class DescendingUtilityComparator implements Comparator<Product> {
    public int compare(Product p1, Product p2)
    {
        if (p1.getUtility() == p2.getUtility()) {
            return Double.compare(p2.getTotal_price(), p1.getTotal_price());
        }
        return Integer.compare(p2.getUtility(), p1.getUtility());
    }
}