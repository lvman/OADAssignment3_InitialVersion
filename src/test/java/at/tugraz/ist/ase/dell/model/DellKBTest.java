package at.tugraz.ist.ase.dell.model;

import at.tugraz.ist.ase.dell.service.Configurator;
import at.tugraz.ist.ase.dell.service.Solution;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;

import java.io.IOException;

public class DellKBTest {
    DellKB dellKb;

    @org.junit.jupiter.api.Test
    void test() throws FeatureModelParserException, IOException {
        // create the DellKB
        dellKb = new DellKB();
        dellKb.init();

        // print components
        dellKb.getProductStructure().getComponents().forEach(component -> {
            System.out.println("Component: " + component.getName());
            component.getOptions().forEach(option -> {
                System.out.println("Option: " + option.getName());
            });
        });

        findAllSolutions();
    }

    private void findAllSolutions() {
        Configurator configurator = new Configurator(dellKb);

        configurator.findAllSolutions(0);
        System.out.println("Number of solutions: " + configurator.getNumberSolutions());
        assert configurator.getNumberSolutions() == 2319;

        for (Solution s : configurator.getSolutions()) {
            System.out.println(s);
        }
    }
}