package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.ml.genetic.GeneticParams;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

public class Spinners extends GridPane
{
    private static final int ORIGINAL_POP = 500;
    private static final int MIN_POP = 200;
    private static final int MAX_POP = 5000;
    private static final int POP_MARGIN = 25;

    private static final double CROSSOVER_RATE = 0.1d;
    private static final double MUTATION_STRENGTH = 2d;
    private static final double MUTATION_RATE = 0.030d;

    private GeneticParams params;

    public Spinners()
    {
        setStyle( "-fx-padding: 10px;" );

        params = new GeneticParams( ORIGINAL_POP, CROSSOVER_RATE, MUTATION_STRENGTH, MUTATION_RATE );

        addIntSpinner("Population", MIN_POP, MAX_POP, ORIGINAL_POP, POP_MARGIN, params::setPopulation, 0 );
        addDoubleSpinner("Crossover rate" , 0.01d, 1d, CROSSOVER_RATE, 0.01d, params::setCrossoverRate, 1 );
        addDoubleSpinner( "Mutation Strength", 0.1d, 5d, MUTATION_STRENGTH, 0.1d, params::setMutationStrength, 2);
        addDoubleSpinner("Mutation rate", 0.001d, 0.100d, MUTATION_RATE, 0.001d, params::setMutationRate, 3);
    }


    private <T extends Number> void addSpinner( String text, Spinner<T> spinner, Consumer<T> func, int i )
    {
        Label label = new Label( text );
        label.setStyle( "-fx-padding: 10px;" );

        spinner.valueProperty().addListener( (e, o, n) -> func.accept(n) );

        ColumnConstraints column = new ColumnConstraints();
        getColumnConstraints().add(column);
        addRow( i, label, spinner );
    }

    private void addIntSpinner(String text, int min, int max, int def, int margin, Consumer<Integer> func, int i)
    {
        Spinner<Integer> spinner = new Spinner<>( min, max, def, margin);
        addSpinner( text, spinner, func, i );
    }

    private void addDoubleSpinner(String text, double min, double max, double def, double margin, Consumer<Double> func, int i )
    {
        Spinner<Double> spinner = new Spinner<>( min, max, def, margin);
        addSpinner( text, spinner, func, i );
    }

    public GeneticParams getParams() { return params; }
}
