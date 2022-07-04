package com.peacefulotter.ml.ui;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class Spinners extends GridPane
{
    private static final int ORIGINAL_POP = 1000;
    private static final int MIN_POP = 200;
    private static final int MAX_POP = 5000;
    private static final int POP_MARGIN = 25;

    private static final double CROSSOVER_RATE = 0.1d;
    private static final double MUTATION_STRENGTH = 2d;
    private static final double MUTATION_RATE = 0.030d;

    private final Spinner<Integer> popSpinner;
    private final Spinner<Double> crossoverSpinner, mutIntensitySpinner, muteRateSpinner;


    public Spinners()
    {
        setStyle( "-fx-padding: 10px;" );

        popSpinner = addIntSpinner("Population", MIN_POP, MAX_POP, ORIGINAL_POP, POP_MARGIN,0);
        crossoverSpinner = addDoubleSpinner("Crossover rate" , 0.01d, 1d, CROSSOVER_RATE, 0.01d, 1 );
        mutIntensitySpinner = addDoubleSpinner( "Mutation Strength", 0.1d, 5d, MUTATION_STRENGTH, 0.1d, 2);
        muteRateSpinner = addDoubleSpinner("Mutation rate", 0.001d, 0.100d, MUTATION_RATE, 0.001d, 3);
    }


    private <T> void addSpinner( String text, Spinner<T> spinner, int i )
    {
        Label label = new Label( text );
        label.setStyle( "-fx-padding: 10px;" );

        ColumnConstraints column = new ColumnConstraints();
        getColumnConstraints().add(column);
        addRow( i, label, spinner );
    }

    private Spinner<Integer> addIntSpinner( String text, int min, int max, int def, int margin, int i  )
    {
        Spinner<Integer> spinner = new Spinner<>( min, max, def, margin);
        addSpinner( text, spinner, i );
        return spinner;
    }

    private Spinner<Double> addDoubleSpinner( String text, double min, double max, double def, double margin, int i  )
    {
        Spinner<Double> spinner = new Spinner<>( min, max, def, margin);
        addSpinner( text, spinner, i );
        return spinner;
    }

    public Integer getPopulation() { return popSpinner.getValue(); }
    public Double getCrossover() { return crossoverSpinner.getValue(); }
    public Double getMutIntensity() { return mutIntensitySpinner.getValue(); }
    public Double getMutRate() { return muteRateSpinner.getValue(); }
}
